package com.tianji.learning.controller;


import com.tianji.common.domain.dto.PageDTO;
import com.tianji.learning.domain.dto.QuestionFormDTO;
import com.tianji.learning.domain.query.QuestionPageQuery;
import com.tianji.learning.domain.vo.QuestionVO;
import com.tianji.learning.service.IInteractionQuestionService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 * 互动提问的问题表 前端控制器
 * </p>
 *
 * @author author
 * @since 2026-08-03
 */
@Slf4j
@RestController
@RequestMapping("/question")
@RequiredArgsConstructor
public class InteractionQuestionController {
    private final IInteractionQuestionService questionService;

    @ApiOperation("新增提问")
    @PostMapping
    public void saveQuestion(@Valid @RequestBody QuestionFormDTO questionDTO) {
        questionService.saveQuestion(questionDTO);
    }

    @ApiOperation("编辑提问")
    @PostMapping
    public void editedQuestion(@Valid @RequestBody QuestionFormDTO questionDTO) {
        questionService.updeteQuestion(questionDTO);
    }

    @ApiOperation("分页查询提问")
    @GetMapping("/page")
    //TODO 未完全了解接口功能逻辑
    public PageDTO<QuestionVO> queryQuestionPage(QuestionPageQuery page) {
        return questionService.queryQuestionPage(page);
    }

    @ApiOperation("根据id查询问题详情")
    @GetMapping("/{id}")
    public QuestionVO queryQuestionById(@ApiParam(value = "问题id", example = "1") @PathVariable("id") Long id){
        return questionService.queryQuestionById(id);
    }

    @ApiOperation("删除问题")
    @DeleteMapping("/{id}")
    public void deleteQuestion(@ApiParam(value = "问题id", example = "1") @PathVariable("id") Long id){
        questionService.deleteQueationById(id);
    }


}

