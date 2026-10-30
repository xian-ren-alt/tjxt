package com.tianji.learning.controller;

import com.tianji.learning.service.IPointsBoardSeasonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 赛季信息表 前端控制器
 * </p>
 *
 * @author author
 * @since 2026-10-27
 */
@Api(tags = "赛季相关接口")
@RestController
@RequestMapping("/seasons")
@RequiredArgsConstructor
public class PointsBoardSeasonController {

    private final IPointsBoardSeasonService pointsBoardSeasonService;

    @ApiOperation("查询所有赛季列表")
    @GetMapping("/list")
    public List<com.tianji.learning.domain.po.PointsBoardSeason> listSeasons() {
        return pointsBoardSeasonService.list();
    }
}
