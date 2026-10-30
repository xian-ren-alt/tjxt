package com.tianji.remark.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(description = "点赞记录信息")
public class LikeVO {

    @ApiModelProperty("点赞记录id")
    private Long id;

    @ApiModelProperty("点赞用户id")
    private Long userId;

    @ApiModelProperty("点赞用户昵称")
    private String userName;

    @ApiModelProperty("点赞用户头像")
    private String userIcon;

    @ApiModelProperty("点赞时间")
    private LocalDateTime createTime;
}
