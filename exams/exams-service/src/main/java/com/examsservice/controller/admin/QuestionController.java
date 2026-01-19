package com.examsservice.controller.admin;

import com.common.result.PageResult;
import com.common.result.Result;
import com.exam.dto.QuestionDTO;
import com.exam.dto.QuestionPageQueryDTO;
import com.exam.vo.QuestionVO;
import com.exam.vo.questionTypeVo;
import com.examsservice.service.QuestionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "题目详细相关文档")
@RestController
@RequestMapping("/question")
@RequiredArgsConstructor
@Slf4j
public class QuestionController {
    private final QuestionService questionService;

    @ApiOperation("新增题目")
    @PostMapping("/add")
    public Result<String> insert(@RequestBody QuestionDTO questionDTO){
        log.info("新增题目：{}",questionDTO);
        questionService.saveWithOption(questionDTO);
        return Result.success();
    }

    @ApiOperation("题目分页查询")
    @GetMapping("/page")
    public Result<PageResult> page( QuestionPageQueryDTO questionPageQueryDTO){

        PageResult pageResult = questionService.pageQuery(questionPageQueryDTO);
        return Result.success(pageResult);
    }

    @ApiOperation("根据id查询题目详情")
    @GetMapping("getById/{id}")
    public Result<QuestionVO> getById(@PathVariable long id){
        return Result.success(questionService.getByIdWith(id));
    }

    @ApiOperation("批量删除题目")
    @DeleteMapping
    public Result delete(@RequestParam List<Long> ids) {
        log.info("批量删除题目参数{}",ids);
        questionService.deleteBatch(ids);
        return Result.success();
    }

    @ApiOperation("修改题目详情")
    @PutMapping
    public Result update(@RequestBody QuestionDTO questionDTO){
        log.info("收到修改参数{}",questionDTO);
        questionService.updateBatch(questionDTO);
        return Result.success();
    }

    @ApiOperation("查询各题型题目数量")
    @GetMapping("/getSumQuestion")
    public Result<List<questionTypeVo>> getSumQuestion(){
        return Result.success(questionService.getBatch());
    }
}
