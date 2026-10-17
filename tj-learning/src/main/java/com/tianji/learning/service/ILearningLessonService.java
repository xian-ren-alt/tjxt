package com.tianji.learning.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tianji.api.dto.trade.OrderBasicDTO;
import com.tianji.common.domain.dto.PageDTO;
import com.tianji.common.domain.query.PageQuery;
import com.tianji.learning.domain.dto.LearningPlanDTO;
import com.tianji.learning.domain.po.LearningLesson;
import com.tianji.learning.domain.vo.LearningLessonVO;
import com.tianji.learning.domain.vo.LearningPlanPageVO;

import java.util.List;

public interface ILearningLessonService extends IService<LearningLesson> {
    void addUserLessons(Long userId, List<Long> courseIds);


    PageDTO<LearningLessonVO> queryMyLessons(PageQuery query);

     LearningLesson queryByUserAndCourseId(Long userId, Long courseId);

    void addLessonPlan(LearningPlanDTO plan);

    LearningPlanPageVO pageQueryLearningPlan(PageQuery query);

    LearningLessonVO queryMyCurrentLesson();

    void deleteLearningCourse(Long userId, List<Long> courseIds);

    void deleteMyCourse(Long courseId);
}
