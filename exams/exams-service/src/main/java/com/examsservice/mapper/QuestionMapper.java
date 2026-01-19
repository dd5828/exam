package com.examsservice.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exam.dto.QuestionPageQueryDTO;
import com.exam.entity.Questions;
import com.exam.vo.QuestionVO;
import com.exam.vo.questionTypeVo;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface QuestionMapper extends BaseMapper<Questions> {

    /**
     * 题目分页
     * @param questionPageQueryDTO
     * @return
     */
    Page<QuestionVO> pageQuery(QuestionPageQueryDTO questionPageQueryDTO);

//    /**
//     * 根据id获取题目详情
//     * @param id
//     * @return
//     */
    QuestionVO getById(long id);

    List<questionTypeVo> getBatch();
}
