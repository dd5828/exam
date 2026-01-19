package com.examsservice.mapper;

import com.exam.dto.StudentDTO;
import com.exam.dto.StudentsPageQueryDTO;
import com.exam.entity.StudentInfo;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 何时去
 * @since 2024-09-02
 */
@Mapper
public interface StudentInfoMapper extends BaseMapper<StudentInfo> {

    /**
     * 学生分页查询
     * @param studentsPageQueryDTO
     * @return
     */
    Page<StudentInfo> pageQuery(StudentsPageQueryDTO studentsPageQueryDTO);

    /**
     * 修改学生信息
     * @param studentDTO
     */
    void updateStudent(StudentDTO studentDTO);
}
