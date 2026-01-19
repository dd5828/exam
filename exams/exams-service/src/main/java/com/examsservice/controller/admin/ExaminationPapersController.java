package com.examsservice.controller.admin;


import com.common.result.PageResult;
import com.common.result.Result;
import com.exam.dto.AddTestPaperQuestionDTO;
import com.exam.dto.ExaminationPapersDTO;
import com.exam.dto.ExamintionPapersPageQueryDTO;
import com.exam.dto.QuestionTypeScore;
import com.exam.vo.ExamintionPapersListVo;
import com.exam.vo.QuestionTypeCountVO;
import com.examsservice.service.IExaminationPapersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 何时去
 * @since 2024-09-21
 */
@RestController
@Api(tags = "试卷相关文档")
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/test/paper")
public class ExaminationPapersController {

    private final IExaminationPapersService paperService;

    @ApiOperation("新增试卷")
    @PostMapping("/add")
    public Result<String> insert(@RequestBody ExaminationPapersDTO examinationPapersDTO){
        log.info("新增试卷参数：{}",examinationPapersDTO);
        int paperId=paperService.insert(examinationPapersDTO);
        return Result.withNumber(paperId);
    }

    @ApiOperation("试卷新增试题")
    @PostMapping("/question")
    public Result<String> insertQuestion(@RequestBody AddTestPaperQuestionDTO questionDTO){
        log.info("试卷新增试题id参数{}",questionDTO);
        paperService.insertQuestion(questionDTO);
        return Result.success();
    }

    @ApiOperation("根据id获取试卷题型数量")
    @GetMapping("/getByIdBatch/{id}")
    public Result<List<QuestionTypeCountVO>> getByIdBatch(@PathVariable Integer id){
        log.info("参数{}",id);
        List<QuestionTypeCountVO> list=paperService.countQuestionTypesByPaperId(id);
        return Result.success(list);
    }

    @ApiOperation("修改试卷信息")
    @PutMapping("/update")
    public Result<String> update(@RequestBody ExaminationPapersDTO papersDTO){
        log.info("修改信息：{}",papersDTO);
        paperService.updateInfo(papersDTO);
        return Result.success();
    }


    @ApiOperation("试卷分页查询")
    @GetMapping("/page")
    public Result<PageResult> page(ExamintionPapersPageQueryDTO dto){
        log.info(String.valueOf(dto));
        PageResult pageResult = paperService.pageQuery(dto);
        return Result.success(pageResult);
    }

    @ApiOperation("根据id删除试卷")
    @DeleteMapping("/delete/{id}")
    public Result<String> delete(@PathVariable int id){
        paperService.delecte(id);
        return Result.success();
    }

    @ApiOperation("根据Id查询试卷详情")
    @GetMapping("/getPaperBy/{id}")
    public Result<ExamintionPapersListVo> getPaperById(@PathVariable int id){
        return Result.success(paperService.getPaperById(id));
    }

    @ApiOperation("设置试卷题型分数")
    @PutMapping("/updateTypeScore")
    public Result<String> updateTypeScore(@RequestBody QuestionTypeScore questionTypeScore){
        paperService.updateTypeScore(questionTypeScore);
        return Result.success();
    }

}
