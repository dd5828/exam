package com.examsservice.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exam.dto.AddTestPaperQuestionDTO;
import com.exam.dto.ExamintionPapersPageQueryDTO;
import com.exam.entity.ExaminationPapers;
import com.exam.vo.ExamintionPapersPageQueryVO;
import com.exam.vo.QuestionTypeCountVO;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 何时去
 * @since 2024-09-21
 */
@Mapper
public interface ExaminationPapersMapper extends BaseMapper<ExaminationPapers> {


    /**
     * 试卷新增题目
     * @param questionDTO
     */
    void batchInsert(AddTestPaperQuestionDTO questionDTO);

    List<QuestionTypeCountVO> countQuestionTypesByPaperId(Integer id);

    /**
     * 修改题型分数
     * @param paperId
     * @param questionType
     * @param average
     */
    void updateTypeScore(long paperId, String questionType, double average);

    /**
     * 试卷分页DTO
     * @param dto
     * @return
     */
    Page<ExamintionPapersPageQueryVO> pageQuery(ExamintionPapersPageQueryDTO dto);

    List<Long> selectExistingQuestionIds(Long paperId, List<Long> questionIds);
}
