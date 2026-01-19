package com.examsservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exam.entity.QuestionOptions;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface QuestionOptionMapper  extends BaseMapper<QuestionOptions> {
    /**
     * 批量插入题目选项
     * @param options
     */
    void insertBatch(List<QuestionOptions> options);

    /**
     * 根据题目id查询选项集合
     * @param id
     * @return
     */
    List<QuestionOptions> getByIdList(long id);
}
