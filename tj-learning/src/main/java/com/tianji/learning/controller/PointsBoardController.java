package com.tianji.learning.controller;

import com.tianji.learning.service.IPointsBoardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 学霸天梯榜 前端控制器
 * </p>
 *
 * @author author
 * @since 2026-10-27
 */
@Api(tags = "积分榜单相关接口")
@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class PointsBoardController {

    private final IPointsBoardService pointsBoardService;

    @ApiOperation("查询当前赛季积分榜")
    @GetMapping("/current")
    public void queryCurrentBoard() {
        // TODO: 查询当前赛季前100名
    }

    @ApiOperation("查询历史赛季积分榜")
    @GetMapping("/history/{season}")
    public void queryHistoryBoard(@org.springframework.web.bind.annotation.PathVariable("season") Integer season) {
        // TODO: 查询指定赛季的榜单
    }

    @ApiOperation("查询我的积分排名")
    @GetMapping("/my-rank")
    public void queryMyRank() {
        // TODO: 查询当前用户在当前赛季的排名
    }
}
