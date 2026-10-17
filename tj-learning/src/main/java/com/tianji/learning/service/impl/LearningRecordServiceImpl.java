package com.tianji.learning.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tianji.api.client.course.CourseClient;
import com.tianji.api.dto.course.CourseFullInfoDTO;
import com.tianji.api.dto.course.CourseSimpleInfoDTO;
import com.tianji.api.dto.leanring.LearningLessonDTO;
import com.tianji.api.dto.leanring.LearningRecordDTO;
import com.tianji.common.exceptions.BizIllegalException;
import com.tianji.common.exceptions.DbException;
import com.tianji.common.utils.BeanUtils;
import com.tianji.common.utils.UserContext;
import com.tianji.learning.domain.dto.LearningRecordFormDTO;
import com.tianji.learning.domain.po.LearningLesson;
import com.tianji.learning.domain.po.LearningRecord;
import com.tianji.learning.enums.SectionType;
import com.tianji.learning.mapper.LearningRecordMapper;
import com.tianji.learning.service.ILearningRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 学习记录表 服务实现类
 * </p>
 *
 * @author author
 * @since 2026-07-23
 */
@RequiredArgsConstructor
@Service
public class LearningRecordServiceImpl extends ServiceImpl<LearningRecordMapper, LearningRecord> implements ILearningRecordService {
    private final LearningLessonServiceImpl lessonService;
    private final CourseClient courseClient;

    @Override
    public LearningLessonDTO queryLearningRecordByCourse(Long courseId) {
        Long userId = UserContext.getUser();
        LearningLesson lesson = lessonService.queryByUserAndCourseId(userId, courseId);

        List<LearningRecord> records = lambdaQuery()
                .eq(LearningRecord::getLessonId, lesson.getId()).list();
        // 4.封装结果
        LearningLessonDTO dto = new LearningLessonDTO();
        dto.setId(lesson.getId());
        dto.setLatestSectionId(lesson.getLatestSectionId());
        dto.setRecords(BeanUtils.copyList(records, LearningRecordDTO.class));

        return dto;
    }

    @Override
    public void commitLearningRecord(LearningRecordFormDTO dto) {
        //1.获取用户信息
        Long userId = UserContext.getUser();
        //2.提交学习记录
        handlecomitRecord(userId, dto);
        //3.更新课程信息
        handlecomitLearningLesson(userId, dto);
    }
    private void handlecomitRecord(Long userId, LearningRecordFormDTO dto) {
        LearningRecord record = BeanUtils.copyBean(dto, LearningRecord.class);
        record.setUpdateTime(dto.getCommitTime());
        boolean finished;
        record.setUserId(userId);
        if (dto.getSectionType() == SectionType.EXAM) {
            record.setFinished(true);
            save(record);
        }
        else {
            finished = record.getMoment()*2 >= dto.getDuration();
            record.setFinished(finished);
            //检查是否是第一次观看，是就插入，不是就更新
            LearningRecord oldRecord =lambdaQuery()
                    .eq(LearningRecord::getLessonId,dto.getLessonId())
                    .eq(LearningRecord::getSectionId, dto.getSectionId())
                    .one();
            if (oldRecord == null) {
                record.setCreateTime(dto.getCommitTime());
                boolean success=save(record);
                if (!success) {
                    throw new DbException("新增考试记录失败！");
                }
            } else {
                record.setId(oldRecord.getId());
                boolean success =updateById(record);
                if (!success) {
                    throw new DbException("更新考试记录失败！");
                }
            }
        }
    }
    //3.更新课程信息
    private void handlecomitLearningLesson(Long userId, LearningRecordFormDTO dto) {
        //需要更新LearningLesson小节信息和时间信息
        LearningLesson lesson = lessonService.queryByUserAndCourseId(userId, dto.getLessonId());
        if (lesson == null) {
            throw new BizIllegalException("课程不存在，无法更新数据！");
        }
        lesson.setLatestSectionId(dto.getSectionId());
        lesson.setLatestLearnTime(dto.getCommitTime());
        lesson.setUpdateTime(dto.getCommitTime());
        lesson.setStatus(1);
        boolean finished = dto.getMoment()*2 >= dto.getDuration();
        if (dto.getSectionType() == SectionType.EXAM) {
            lesson.setLearnedSections(lesson.getLearnedSections() + 1);
        }else {
            if (finished){
                lesson.setLearnedSections(lesson.getLearnedSections() + 1);
            }
        }
        //根据课程完成章节数率判断课程完成状态
        CourseFullInfoDTO courseInfo = courseClient.getCourseInfoById(lesson.getCourseId(), false, false);
        if (courseInfo == null) {
            throw new BizIllegalException("课程不存在，无法更新数据！");
        }
        if (lesson.getLearnedSections() >= courseInfo.getSectionNum()) {
            lesson.setPlanStatus(2);
        }
        lessonService.updateById(lesson);
    }
}
