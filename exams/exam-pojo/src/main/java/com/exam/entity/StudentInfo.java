package com.exam.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 学生信息实体类
 */
@Data
@Builder//允许使用构建器模式创建对象
@NoArgsConstructor//为类生成一个无参构造函数
@AllArgsConstructor//为类生成一个包含所有字段的构造函数
public class StudentInfo {
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
    /**
     * 学生信息创建时间
     */
    private LocalDateTime createTime;
    /**
     * 学生信息最后更新时间
     */
    private LocalDateTime updateTime;}
