package generator.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;

import java.io.Serializable;

import java.util.Date;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

/**
* 学习记录表
* @TableName learning_record
*/
public class LearningRecord implements Serializable {

    /**
    * 学习记录的id
    */
    @NotNull(message="[学习记录的id]不能为空")
    @ApiModelProperty("学习记录的id")
    private Long id;
    /**
    * 对应课表的id
    */
    @NotNull(message="[对应课表的id]不能为空")
    @ApiModelProperty("对应课表的id")
    private Long lessonId;
    /**
    * 对应小节的id
    */
    @NotNull(message="[对应小节的id]不能为空")
    @ApiModelProperty("对应小节的id")
    private Long sectionId;
    /**
    * 用户id
    */
    @NotNull(message="[用户id]不能为空")
    @ApiModelProperty("用户id")
    private Long userId;
    /**
    * 视频的当前观看时间点，单位秒
    */
    @ApiModelProperty("视频的当前观看时间点，单位秒")
    private Integer moment;
    /**
    * 是否完成学习，默认false
    */
    @NotNull(message="[是否完成学习，默认false]不能为空")
    @ApiModelProperty("是否完成学习，默认false")
    private Boolean finished;
    /**
    * 第一次观看时间
    */
    @NotNull(message="[第一次观看时间]不能为空")
    @ApiModelProperty("第一次观看时间")
    private Date createTime;
    /**
    * 更新时间（最近一次观看时间）
    */
    @NotNull(message="[更新时间（最近一次观看时间）]不能为空")
    @ApiModelProperty("更新时间（最近一次观看时间）")
    private Date updateTime;

    /**
    * 学习记录的id
    */
    private void setId(Long id){
    this.id = id;
    }

    /**
    * 对应课表的id
    */
    private void setLessonId(Long lessonId){
    this.lessonId = lessonId;
    }

    /**
    * 对应小节的id
    */
    private void setSectionId(Long sectionId){
    this.sectionId = sectionId;
    }

    /**
    * 用户id
    */
    private void setUserId(Long userId){
    this.userId = userId;
    }

    /**
    * 视频的当前观看时间点，单位秒
    */
    private void setMoment(Integer moment){
    this.moment = moment;
    }

    /**
    * 是否完成学习，默认false
    */
    private void setFinished(Boolean finished){
    this.finished = finished;
    }

    /**
    * 第一次观看时间
    */
    private void setCreateTime(Date createTime){
    this.createTime = createTime;
    }

    /**
    * 更新时间（最近一次观看时间）
    */
    private void setUpdateTime(Date updateTime){
    this.updateTime = updateTime;
    }


    /**
    * 学习记录的id
    */
    private Long getId(){
    return this.id;
    }

    /**
    * 对应课表的id
    */
    private Long getLessonId(){
    return this.lessonId;
    }

    /**
    * 对应小节的id
    */
    private Long getSectionId(){
    return this.sectionId;
    }

    /**
    * 用户id
    */
    private Long getUserId(){
    return this.userId;
    }

    /**
    * 视频的当前观看时间点，单位秒
    */
    private Integer getMoment(){
    return this.moment;
    }

    /**
    * 是否完成学习，默认false
    */
    private Boolean getFinished(){
    return this.finished;
    }

    /**
    * 第一次观看时间
    */
    private Date getCreateTime(){
    return this.createTime;
    }

    /**
    * 更新时间（最近一次观看时间）
    */
    private Date getUpdateTime(){
    return this.updateTime;
    }

}
