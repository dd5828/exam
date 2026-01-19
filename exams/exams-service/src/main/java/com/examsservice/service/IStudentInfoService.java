package com.examsservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.common.result.PageResult;
import com.exam.dto.StudentDTO;
import com.exam.dto.StudentsPageQueryDTO;
import com.exam.entity.StudentInfo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 何时去
 * @since 2024-09-02
 */
public interface IStudentInfoService extends IService<StudentInfo> {

    /**
     * 新增学生
     * @param studentDTO
     */
    void insertStudent(StudentDTO studentDTO);

    /**
     * 学生分页查询
     * @param studentsPageQueryDTO
     * @return
     */
    PageResult pageQuery(StudentsPageQueryDTO studentsPageQueryDTO);

    /**
     * 修改学生信息
     * @param studentDTO
     */
    void updateStudentInfo(StudentDTO studentDTO);
}
