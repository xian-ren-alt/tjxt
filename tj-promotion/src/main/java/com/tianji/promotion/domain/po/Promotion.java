package com.tianji.promotion.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 促销活动，形式多种多样，例如：优惠券
 * </p>
 *
 * @author author
 * @since 2026-08-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("promotion")
public class Promotion implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 促销活动id
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private Long id;

    /**
     * 活动名称
     */
    @TableField("name")
    private String name;

    /**
     * 促销活动类型：1-优惠券，2-分销
     */
    @TableField("type")
    private Integer type;

    /**
     * 是否是热门活动：true或false，默认false
     */
    @TableField("hot")
    private Integer hot;

    /**
     * 活动开始时间
     */
    @TableField("begin_time")
    private LocalDateTime beginTime;

    /**
     * 活动结束时间
     */
    @TableField("end_time")
    private LocalDateTime endTime;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    @TableField("creater")
    private Long creater;

    /**
     * 更新人
     */
    @TableField("updater")
    private Long updater;


}
