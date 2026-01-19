package com.examsservice.controller.admin;


import com.common.result.PageResult;
import com.common.result.Result;
import com.exam.dto.StudentDTO;
import com.exam.dto.StudentsPageQueryDTO;
import com.exam.entity.StudentInfo;
import com.examsservice.service.IStudentInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 何时去
 * @since 2024-09-02
 */
@Controller
@RequestMapping("/student")
@Api(tags = "学生相关接口")
@RestController
@Slf4j
@RequiredArgsConstructor
public class StudentInfoController {

    private final IStudentInfoService iStudentInfoService;

    @ApiOperation("新增学生接口")
    @PostMapping("/insert")
    public Result<String> insert(@RequestBody StudentDTO studentDTO){
        log.info("学生注册信息：{}",studentDTO);
        iStudentInfoService.insertStudent(studentDTO);
        return Result.success();
    }
    @GetMapping("/page")
    @ApiOperation("学生分页查询")
    public Result<PageResult> page(StudentsPageQueryDTO studentsPageQueryDTO){
        PageResult pageResult = iStudentInfoService.pageQuery(studentsPageQueryDTO);
        return Result.success(pageResult);
    }

    @PutMapping("/update")
    @ApiOperation("修改学生信息")
    public Result<String> update(@RequestBody StudentDTO studentDTO){
        log.info("学生修改信息：{}",studentDTO);
        iStudentInfoService.updateStudentInfo(studentDTO);
        return Result.success();
    }
}
