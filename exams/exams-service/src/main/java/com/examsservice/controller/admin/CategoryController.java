package com.examsservice.controller.admin;

import com.common.result.Result;
import com.exam.dto.CategoryDTO;
import com.exam.entity.Category;
import com.examsservice.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "题目分类相关文档")
@RequiredArgsConstructor
@RequestMapping("/category")
@RestController
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    @ApiOperation("新增题目分类")
    @PostMapping("/add")
    public Result<String> insert(@RequestBody CategoryDTO categoryDTO){
        log.info("新增题目参数{}",categoryDTO);
        categoryService.insert(categoryDTO);
        return Result.success();
    }

    @ApiOperation("查询题目分类")
    @GetMapping("/list")
    public Result<List<Category>> list(){
        List<Category> categories=categoryService.list();
        return Result.success(categories);
    }

}
