package com.tianji.promotion.controller;


import com.tianji.common.domain.dto.PageDTO;
import com.tianji.promotion.domain.dto.CouponFormDTO;
import com.tianji.promotion.domain.dto.CouponIssueFormDTO;
import com.tianji.promotion.domain.query.CouponQuery;
import com.tianji.promotion.domain.vo.CouponPageVO;
import com.tianji.promotion.service.ICouponService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Api(tags = "优惠券管理接口")
@RestController
@RequestMapping("/coupon")
@RequiredArgsConstructor
public class CouponController {
    private final ICouponService couponService;
    @ApiOperation("新增优惠券管理接口")
    @PostMapping()
    public void saveCoupon(@RequestBody CouponFormDTO dto){
        couponService.saveCoupon(dto);
    }

    @ApiOperation(value = "分页查询优惠券接口")
    @GetMapping("/page")
    public PageDTO<CouponPageVO> CouponsQuery(CouponQuery query){
        return couponService.CouponsQuery(query);
    }

    @ApiOperation("发放优惠券接口")
    @PutMapping("/{id}/issue")
    public void beginIssue(@RequestBody @Valid CouponIssueFormDTO dto) {
        couponService.beginIssue(dto);
    }

}



