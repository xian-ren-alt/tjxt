package com.tianji.learning.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.tianji.learning.enums.QuestionStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 互动提问的问题表
 * </p>
 *
 * @author author
 * @since 2026-08-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("interaction_question")
public class InteractionQuestion implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键，互动问题的id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 互动问题的标题
     */
    @TableField("title")
    private String title;

    /**
     * 问题描述信息
     */
    @TableField("description")
    private String description;

    /**
     * 所属课程id
     */
    @TableField("course_id")
    private Long courseId;

    /**
     * 所属课程章id
     */
    @TableField("chapter_id")
    private Long chapterId;

    /**
     * 所属课程节id
     */
    @TableField("section_id")
    private Long sectionId;

    /**
     * 提问学员id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 最新的一个回答的id
     */
    @TableField("latest_answer_id")
    private Long latestAnswerId;

    /**
     * 问题下的回答数量
     */
    @TableField("answer_times")
    private Integer answerTimes;

    /**
     * 是否匿名，默认false
     */
    @TableField("anonymity")
    private Boolean anonymity;

    /**
     * 是否被隐藏，默认false
     */
    @TableField("hidden")
    private Boolean hidden;

    /**
     * 管理端问题状态：0-未查看，1-已查看
     */
    @TableField("status")
    private QuestionStatus status;

    /**
     * 提问时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;


}
