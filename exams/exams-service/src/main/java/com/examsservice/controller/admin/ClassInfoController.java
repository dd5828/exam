package com.examsservice.controller.admin;


import com.common.result.PageResult;
import com.common.result.Result;
import com.exam.dto.ClassInfoDTO;
import com.exam.dto.StudentsPageQueryDTO;
import com.exam.entity.ClassInfo;
import com.examsservice.mapper.ClassInfoMapper;
import com.examsservice.service.IClassInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 何时去
 * @since 2024-09-02
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@Api(tags = "班级相关文档")
@RequestMapping("/class")
public class ClassInfoController {

    private final IClassInfoService iClassInfoService;
    private final ClassInfoMapper classInfoMapper;

    @PostMapping("/add")
    @ApiOperation("新增班级")
    public Result<String> insert(@RequestBody ClassInfoDTO classInfoDTO){
        log.info("收到班级注册参数：{}",classInfoDTO);
        iClassInfoService.insertClass(classInfoDTO);
        return Result.success();
    }


    @PostMapping("/getById/{id}")
    @ApiOperation("根据ID查询班级信息")
    public Result<ClassInfo> getById(@PathVariable int id){
        ClassInfo classInfo = iClassInfoService.getClassById(id);
        return Result.success(classInfo);
    }

    @GetMapping("/list")
    @ApiOperation("班级查询")
    public Result<List<ClassInfo>> list(){
        List<ClassInfo> classInfos = classInfoMapper.selectList(null);
        return Result.success(classInfos);
    }
}
