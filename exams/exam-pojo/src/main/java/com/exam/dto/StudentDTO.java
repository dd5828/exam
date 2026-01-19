package com.exam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 学生注册DTO
 */
@Data
public class StudentDTO {
    /**
     * 学生的唯一标识 ID
     */
    private int studentId;
    /**
     * 学生姓名
     */
    private String studentName;
    /**
     * 学生学号
     */
    private String studentNumber;
    /**
     * 学生所属班级的 ID
     */
    private int classId;
    /**
     * 学生密码
     */
    private String password;
    }
