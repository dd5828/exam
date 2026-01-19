package com.exam.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 试卷题型数量返回
 */
@Data
@ApiModel("试卷题型数量视图对象")
public class QuestionTypeCountVO {

    @ApiModelProperty("题型")
    private String questionType;

    @ApiModelProperty("题型数量")
    private Long count;

    @ApiModelProperty("题型分数")
    private Long typeScore;

}
