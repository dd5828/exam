package com.exam.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 题目分页查询DTO
 */
@Data
@ApiModel("题目分页数据传输对象")
public class QuestionPageQueryDTO {

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("题型")
    private String questionType;

    @ApiModelProperty("题目所属分类 ID")
    private Long categoryId;

    @ApiModelProperty("页码")
    private int page;

    //每页显示记录数
    @ApiModelProperty("每页显示记录数")
    private int pageSize;
}
