package com.exam.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 题目选项实体
 */
@Data
@ApiModel("题目选项实体")
public class QuestionOptions {
    @ApiModelProperty("选项主键 ID")
    private Long id;
    @ApiModelProperty("题目 ID")
    private Long questionId;
    @ApiModelProperty("选项内容")
    private String optionContent;
    @ApiModelProperty("是否是正确答案")
    private Long isCorrect;
}
