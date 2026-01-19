package com.exam.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 试卷DTO
 */
@Data
public class ExaminationPapersDTO {
    @ApiModelProperty(value = "试卷编号，自增主键")

    private Integer paperId;

    @ApiModelProperty(value = "试卷标题")
    private String paperTitle;

    @ApiModelProperty(value = "试卷描述")
    private String description;

    @ApiModelProperty(value = "试卷分类id")
    private Long categoryId;

    @ApiModelProperty(value = "考试时长（分钟）")
    private Integer duration;

    @ApiModelProperty(value = "试卷总分")
    private Integer totalScore;

    @ApiModelProperty(value = "试卷状态，1：启用，0：禁用")
    private Integer status;

    @ApiModelProperty(value = "考试开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "考试结束时间")
    private LocalDateTime endTime;


    @ApiModelProperty(value = "试卷图片")
    private String paperPictures;
}
