package com.tianji.learning.controller;


import com.tianji.api.dto.leanring.LearningLessonDTO;
import com.tianji.learning.domain.dto.LearningRecordFormDTO;
import com.tianji.learning.service.ILearningRecordService;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 学习记录表 前端控制器
 * </p>
 *
 * @author author
 * @since 2026-07-23
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/learning-record")
public class LearningRecordController {
    private final ILearningRecordService recordService;

    //查询学习计划
    @GetMapping("/course/{courseId}")
    public LearningLessonDTO queryLearningRecordByCourse(
            @ApiParam(value = "课程id", example = "2") @PathVariable("courseId") Long courseId){
        return recordService.queryLearningRecordByCourse(courseId);
    }

    //提交学习记录
    @PostMapping()
    public void addLearningRecord(@RequestBody LearningRecordFormDTO FormDTO){
        recordService.commitLearningRecord(FormDTO);
    }




}
