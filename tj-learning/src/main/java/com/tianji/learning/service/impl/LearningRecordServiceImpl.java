package com.tianji.learning.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tianji.api.client.course.CourseClient;
import com.tianji.api.dto.course.CourseFullInfoDTO;
import com.tianji.api.dto.course.CourseSimpleInfoDTO;
import com.tianji.api.dto.leanring.LearningLessonDTO;
import com.tianji.api.dto.leanring.LearningRecordDTO;
import com.tianji.common.autoconfigure.mq.RabbitMqHelper;
import com.tianji.common.constants.MqConstants;
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
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    private final StringRedisTemplate redisTemplate;
    private final RabbitMqHelper rabbitMqHelper;

    private static final String COMMIT_RECORD_KEY = "record:lesson:";

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
        dto.setUserId(userId);
        //2.提交学习记录
        handlecomitRecord(userId, dto);
    }

    private void handlecomitRecord(Long userId, LearningRecordFormDTO dto) {
        if (dto.getSectionType() == SectionType.EXAM) {
            LearningRecord record = BeanUtils.copyBean(dto, LearningRecord.class);
            record.setUserId(dto.getUserId());
            record.setFinished(true);
            record.setUpdateTime(dto.getCommitTime());
        }
        redisTemplate.opsForHash().put(
                COMMIT_RECORD_KEY + dto.getLessonId().toString()
                , dto.getSectionId().toString()
                , dto.getMoment().toString());
        redisTemplate.expire(COMMIT_RECORD_KEY + dto.getLessonId(), 60, TimeUnit.SECONDS);

        // 2. 发延迟消息：4个参数 = 交换机、routingKey、消息内容、延迟时间
        rabbitMqHelper.sendDelayMessage(
                MqConstants.Exchange.LEARNING_DELAY_EXCHANGE,   // 交换机
                MqConstants.Key.LEARNING_RECORD_DELAY_KEY,     // routingKey
                dto,                                            // 消息体
                Duration.ofSeconds(30)                         // 延迟30秒
        );


    }


    //3.更新课程信息
    @Override
    public void handlecomitLearningLesson(LearningRecordFormDTO dto) {
        //需要更新LearningLesson小节信息和时间信息
        LearningLesson lesson = lessonService.getById(dto.getLessonId());
        if (lesson == null) {
            throw new BizIllegalException("课程不存在，无法更新数据！");
        }
        lesson.setLatestSectionId(dto.getSectionId());
        lesson.setLatestLearnTime(dto.getCommitTime());
        lesson.setUpdateTime(dto.getCommitTime());
        lesson.setStatus(1);
        boolean finished = dto.getDuration() != null && dto.getMoment() * 2 >= dto.getDuration();
        if (dto.getSectionType() == SectionType.EXAM) {
            lesson.setLearnedSections(lesson.getLearnedSections() + 1);
        } else {
            if (finished) {
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

    public void saveLearningRecord(LearningRecordFormDTO dto) {
        LearningRecord oldRecord = lambdaQuery()
                .eq(LearningRecord::getLessonId, dto.getLessonId())
                .eq(LearningRecord::getSectionId, dto.getSectionId())
                .one();

        LearningRecord record = BeanUtils.copyBean(dto, LearningRecord.class);
        record.setUserId(dto.getUserId());
        record.setUpdateTime(dto.getCommitTime());

        if (oldRecord == null) {
            record.setCreateTime(dto.getCommitTime());
            save(record);              // INSERT
        } else {
            record.setId(oldRecord.getId());
            updateById(record);        // UPDATE
        }
    }

}
