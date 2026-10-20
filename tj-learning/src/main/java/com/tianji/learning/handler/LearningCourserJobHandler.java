package com.tianji.learning.handler;

import com.tianji.learning.service.ILearningLessonService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class LearningCourserJobHandler {
    private final ILearningLessonService lessonService;
    @XxlJob("courseExpireCheckHandler")
    public void checkCourseExpire(){
        lessonService.checkCourseExpire();

    }
}
