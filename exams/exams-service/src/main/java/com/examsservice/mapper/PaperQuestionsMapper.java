package com.examsservice.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exam.entity.PaperQuestions;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 何时去
 * @since 2024-09-18
 */
@Mapper
public interface PaperQuestionsMapper extends BaseMapper<PaperQuestions> {

    PaperQuestions getById(int id, Integer questionId);

}
