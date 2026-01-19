package com.exam.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("题型视图对象")
public class questionTypeVo {
    @ApiModelProperty("题型")
    private String questionType;
    @ApiModelProperty("选题数量")
    private int sumQuestion;
}
