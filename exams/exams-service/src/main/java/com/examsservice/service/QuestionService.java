package com.examsservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.common.result.PageResult;
import com.exam.dto.QuestionDTO;
import com.exam.dto.QuestionPageQueryDTO;
import com.exam.entity.Questions;
import com.exam.vo.QuestionVO;
import com.exam.vo.questionTypeVo;

import java.util.List;


public interface QuestionService extends IService<Questions> {

    /**
     * 新增题目
     * @param questionDTO
     */
    void saveWithOption(QuestionDTO questionDTO);

    /**
     * 题目分页查询
     * @param questionPageQueryDTO
     * @return
     */
    PageResult pageQuery(QuestionPageQueryDTO questionPageQueryDTO);

    /**
     * 根据id查询题目详情
     * @param id
     * @return
     */
    QuestionVO getByIdWith(long id);

    /**
     * 批量删除题目
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    /**
     * 修改题目
     * @param questionDTO
     */
    void updateBatch(QuestionDTO questionDTO);

    /**
     * 查询各题型题目数量
     * @return
     */
    List<questionTypeVo> getBatch();
}
