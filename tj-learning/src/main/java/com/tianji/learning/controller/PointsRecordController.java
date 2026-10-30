package com.tianji.learning.controller;

import com.tianji.learning.service.IPointsRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 学习积分记录 前端控制器
 * </p>
 *
 * @author author
 * @since 2026-10-27
 */
@Api(tags = "积分记录相关接口")
@RestController
@RequestMapping("/points")
@RequiredArgsConstructor
public class PointsRecordController {

    private final IPointsRecordService pointsRecordService;

    @ApiOperation("查询我的积分明细")
    @GetMapping("/my")
    public void queryMyPoints() {
        // TODO: 查询当前用户的积分记录
    }

    @ApiOperation("查询我的总积分")
    @GetMapping("/my-total")
    public void queryMyTotalPoints() {
        // TODO: 统计当前用户总积分
    }
}
