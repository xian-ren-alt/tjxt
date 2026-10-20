package com.tianji.learning.controller;

import com.tianji.api.dto.trade.OrderBasicDTO;
import com.tianji.common.domain.dto.PageDTO;
import com.tianji.common.domain.query.PageQuery;
import com.tianji.learning.domain.dto.LearningPlanDTO;
import com.tianji.learning.domain.vo.LearningLessonVO;
import com.tianji.learning.domain.vo.LearningPlanPageVO;
import com.tianji.learning.service.ILearningLessonService;
import com.xxl.job.core.handler.annotation.XxlJob;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags = "我的课表相关接口")
@RestController
@RequestMapping("/lessons")
@RequiredArgsConstructor
public class LearningLessonController {

    private final ILearningLessonService lessonService;

    @ApiOperation("查询我的课表，排序字段 latest_learn_time:学习时间排序，create_time:购买时间排序")
    @GetMapping("/page")
    public PageDTO<LearningLessonVO> queryMyLessons(PageQuery query) {
        return lessonService.queryMyLessons(query);
    }

    //创建学习记录
    @ApiOperation("创建学习记录")
    @PostMapping("/plan")
    public void createLearningPlan(LearningPlanDTO  plan) {
        lessonService.addLessonPlan(plan);
    }

    //分页查询学习计划进度
    @ApiOperation("分页查询学习计划进度")
    @GetMapping("/plan")
    public LearningPlanPageVO queryLearningPlan(PageQuery query) {
        return lessonService.pageQueryLearningPlan(query);
    }

    //查询正在学习的课程
    @ApiOperation("查询正在学习的课程")
    @GetMapping("/now")
    public LearningLessonVO queryMyCurrentLesson(){
        return lessonService.queryMyCurrentLesson();
    }

    //删除过期课程
    @ApiOperation("删除课程")
    @DeleteMapping("/{courseId}")
    public void deleteLearningCourse(@PathVariable("courseId") Long courseId){
        lessonService.deleteMyCourse(courseId);
    }

}
