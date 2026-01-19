package com.exam.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 题目分页查询DTO
 */
@Data
@ApiModel("试卷分页数据传输对象")
public class ExamintionPapersPageQueryDTO {

    @ApiModelProperty("试卷标题")
    private String paperTitle;

    @ApiModelProperty("试卷状态")
    private Integer status;


    @ApiModelProperty("页码")
    private int page;

    //每页显示记录数
    @ApiModelProperty("每页显示记录数")
    private int pageSize;
}
