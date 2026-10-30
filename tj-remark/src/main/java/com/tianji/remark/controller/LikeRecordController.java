package com.tianji.remark.controller;

import com.tianji.remark.domain.dto.LikeRecordFormDTO;
import com.tianji.remark.service.ILikeRecordService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 点赞 前端控制器
 * </p>
 *
 * @author author
 * @since 2026-10-21
 */
@RestController
@RequestMapping("/likes")
@RequiredArgsConstructor
public class LikeRecordController {

    private final ILikeRecordService likeRecordService;

    @PostMapping
    @ApiOperation("点赞或取消点赞")
    public void addLikeRecord(@Valid @RequestBody LikeRecordFormDTO recordDTO) {
        likeRecordService.addLikeRecord(recordDTO);
    }

    @GetMapping("list")
    @ApiOperation("查询指定业务id的点赞状态")
    public Set<Long> isBizLiked(@RequestParam("bizIds") List<Long> bizIds){
        return likeRecordService.isBizLiked(bizIds);
    }
}
