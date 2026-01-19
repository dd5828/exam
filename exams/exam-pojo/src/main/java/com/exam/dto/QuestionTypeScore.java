package com.exam.dto;

import com.exam.vo.QuestionTypeCountVO;
import lombok.Data;

import java.util.List;

/**
 * 设置试卷题型分数DTO
 */
@Data
public class QuestionTypeScore {
    private long paperId;//试卷ID
    private List<QuestionTypeCountVO> questionTypeCountVOList;//题型分数
}
