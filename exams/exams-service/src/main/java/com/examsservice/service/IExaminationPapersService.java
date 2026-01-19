package com.examsservice.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.common.result.PageResult;
import com.exam.dto.AddTestPaperQuestionDTO;
import com.exam.dto.ExaminationPapersDTO;
import com.exam.dto.ExamintionPapersPageQueryDTO;
import com.exam.dto.QuestionTypeScore;
import com.exam.entity.ExaminationPapers;
import com.exam.vo.ExamintionPapersListVo;
import com.exam.vo.QuestionTypeCountVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 何时去
 * @since 2024-09-21
 */
public interface IExaminationPapersService extends IService<ExaminationPapers> {

    /**
     * 新增试卷
     * @param examinationPapersDTO
     */
    Integer insert(ExaminationPapersDTO examinationPapersDTO);


    /**
     * 试卷新增试题
     * @param questionDTO
     */
    void insertQuestion(AddTestPaperQuestionDTO questionDTO);

    /**
     * 根据试卷id查询题型数量
     */


    /**
     * 根据id查询题型数量
     * @param id
     * @return
     */
    List<QuestionTypeCountVO> countQuestionTypesByPaperId(Integer id);

    /**
     * 修改试卷
     */
    void updateInfo(ExaminationPapersDTO papersDTO);

    /**
     * 设置题型分数
     * @param typeScore
     */
    void updateTypeScore(QuestionTypeScore typeScore);

    /**
     * 试卷分页查询
     * @param dto
     * @return
     */
    PageResult pageQuery(ExamintionPapersPageQueryDTO dto);

    /**
     * 根据id删除试卷
     * @param id
     */
    void delecte(int id);

    /**
     * 根据Id查询试卷详情
     * @param id
     * @return
     */
    ExamintionPapersListVo getPaperById(int id);
}
