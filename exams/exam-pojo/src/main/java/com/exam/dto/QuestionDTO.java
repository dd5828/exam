package com.exam.dto;

import com.exam.entity.QuestionOptions;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 题目数据传输对象
 */
@Data
@ApiModel("题目数据传输对象")
public class QuestionDTO {
    @ApiModelProperty("题目主键 ID")
    private Long id;
    @ApiModelProperty("题目标题")
    private String title;
    @ApiModelProperty("题目标签")
    private String tags;
    @ApiModelProperty("题目所属分类 ID")
    private Long categoryId;
    @ApiModelProperty("题型")
    private String questionType;

    //题目选项
    @ApiModelProperty("题目选项")
    private List<QuestionOptions> questionOptions=new ArrayList<>();
}
