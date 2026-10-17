package com.tianji.learning.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("learning_lesson")
public class LearningLesson {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 课程id
     */
    @TableField("course_id")
    private Long courseId;

    /**
     * 课程状态：0-未学习 1-学习中 2-已学完 3-已失效
     */
    private Integer status;

    /**
     * 每周学习频率，6,每周学习频率为6
     */
    @TableField("week_freq")
    private Integer weekFreq;

    /**
     * 学习计划状态： 0- 没有学习计划 1-计划进行中
     */
    @TableField("plan_status")
    private Integer planStatus;

    /**
     * 已学小节数
     */
    @TableField("learned_sections")
    private Integer learnedSections;

    /**
     * 最近一次学习小节id
     */
    @TableField("latest_section_id")
    private Long latestSectionId;

    /**
     * 最新学习时间
     */
    @TableField("latest_learn_time")
    private LocalDateTime latestLearnTime;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 过期时间
     */
    @TableField("expire_time")
    private LocalDateTime expireTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;
}
