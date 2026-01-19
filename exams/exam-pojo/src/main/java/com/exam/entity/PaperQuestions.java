package com.exam.entity;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;

/**
 * <p>
 * 试卷与题目关联实体
 * </p>
 *
 * @author 何时去
 * @since 2024-09-18
 */
@Getter
@Data
@ApiModel(value="PaperQuestions对象", description="")
public class PaperQuestions implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增主键")
    private Integer id;

    @ApiModelProperty(value = "试卷编号（逻辑外键）")
    private Integer paperId;

    @ApiModelProperty(value = "题目编号（逻辑外键）")
    private Integer questionId;

    @ApiModelProperty("题目分数")
    private double typeScore;
}
