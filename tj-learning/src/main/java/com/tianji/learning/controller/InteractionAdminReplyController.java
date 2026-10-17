package com.tianji.learning.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import io.swagger.annotations.ApiParam;

import com.tianji.common.domain.dto.PageDTO;
import com.tianji.learning.domain.query.ReplyPageQuery;
import com.tianji.learning.domain.vo.ReplyVO;
import com.tianji.learning.service.IInteractionReplyService;

import groovy.util.logging.Slf4j;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Slf4j
@RestController("/admin/replies")
@RequiredArgsConstructor
public class InteractionAdminReplyController {

    private final IInteractionReplyService iInteractionReplyService;

    @ApiOperation("管理端分页查询回答详情")
    @GetMapping("/page")
    public PageDTO<ReplyVO> pageQuert(ReplyPageQuery query){
        return iInteractionReplyService.adminPageQueryReply(query);
    }

    @ApiOperation("管理端显示或隐藏回答/评论")
    @PutMapping("/{id}/hidden/{hidden}")
    public void updateHidden(
            @PathVariable @ApiParam("回答或评论id") Long id,
            @PathVariable @ApiParam("是否隐藏") Boolean hidden){
        iInteractionReplyService.updateReplyHidden(id, hidden);
    }

}
