package com.examsservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exam.dto.CategoryDTO;
import com.exam.entity.Category;

public interface CategoryService extends IService<Category> {
    /**
     * 新增题目分类
     * @param categoryDTO
     */
    void insert(CategoryDTO categoryDTO);
}
