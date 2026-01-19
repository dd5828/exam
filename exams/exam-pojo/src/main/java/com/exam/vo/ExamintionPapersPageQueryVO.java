package com.exam.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 试卷分页VO
 */
@Data
public class ExamintionPapersPageQueryVO {
    @ApiModelProperty(value = "试卷编号，自增主键")
    @TableId(type = IdType.AUTO)
    private Integer paperId;

    @ApiModelProperty(value = "试卷标题")
    private String paperTitle;

    @ApiModelProperty(value = "试卷描述")
    private String description;

    @ApiModelProperty(value = "试卷分类")
    private String categoryName;

    @ApiModelProperty(value = "考试时长（分钟）")
    private Integer duration;

    @ApiModelProperty(value = "试卷总分")
    private Integer totalScore;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updatedAt;

    @ApiModelProperty(value = "试卷状态，1：启用，0：禁用")
    private Integer status;

    @ApiModelProperty(value = "考试开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "考试结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "创建用户")
    private String createUser;

    @ApiModelProperty(value = "修改用户")
    private String updateUser;

    @ApiModelProperty(value = "试卷图片")
    private String paperPictures;
}
