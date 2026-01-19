package com.examsservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.constant.MessageConstant;
import com.common.context.BaseContext;
import com.common.exception.AccountNotFoundException;
import com.exam.dto.CategoryDTO;
import com.exam.entity.Category;

import com.examsservice.mapper.CategoryMapper;

import com.examsservice.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    private final CategoryMapper categoryMapper;

    /**
     * 新增题目分类
     * @param categoryDTO
     */
    @Override
    public void insert(CategoryDTO categoryDTO) {
        //1.判断分类名称是否为空
        if(categoryDTO.getTypeName()==null){
            throw new AccountNotFoundException(MessageConstant.Type_Name_Found);
        }
        //2.创建对象并存入
        //2.1获取当前登录用户id-----从线程中获取id
        Long userId = BaseContext.getCurrentId();
        Category category=Category.builder()
                .typeName(categoryDTO.getTypeName())
                .createUser(userId)
                .updateUser(userId)
                .updateTime(new Date())
                .createTime(new Date())
                .build();
        categoryMapper.insert(category);

    }
}
