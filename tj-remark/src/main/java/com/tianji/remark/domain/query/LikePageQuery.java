package com.tianji.remark.domain.query;

import com.tianji.common.domain.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "点赞记录分页查询条件")
public class LikePageQuery extends PageQuery {

    @ApiModelProperty(value = "点赞的业务id")
    private Long bizId;

    @ApiModelProperty(value = "点赞的业务类型")
    private String bizType;
}
