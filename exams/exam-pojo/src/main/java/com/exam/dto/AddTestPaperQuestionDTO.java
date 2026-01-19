package com.exam.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 试卷新增试题DTO
 */
@Data
@ApiModel("试卷试题数据传输对象")
public class AddTestPaperQuestionDTO {

    @ApiModelProperty("试卷id")
    private long paperId;

    @ApiModelProperty("题目id集合")
    private List<Long> ids;
}
