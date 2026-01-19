package com.exam.vo;

import com.exam.dto.QuestionDTO;
import com.exam.entity.ExaminationPapers;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 试卷详情VO
 */
@Data
@ApiOperation("试卷查询对应题目视图")
public class ExamintionPapersListVo {

    @ApiModelProperty("试卷详情")
    private ExaminationPapers paper;

    @ApiModelProperty("题目信息")
    private List<QuestionVO> questionVOList=new ArrayList<>();
}
