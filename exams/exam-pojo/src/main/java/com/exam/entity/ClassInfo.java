package com.exam.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 班级信息实体类
 */
@Data
@Builder//允许使用构建器模式创建对象
@NoArgsConstructor//为类生成一个无参构造函数
@AllArgsConstructor//为类生成一个包含所有字段的构造函数
public class ClassInfo {
    /**
     * 班级的唯一标识 ID
     */
    private int classId;
    /**
     * 班级名称
     */
    private String className;
    /**
     * 专业
     */
    private String major;
    /**
     * 班级信息创建时间
     */
    private LocalDateTime createTime;
    /**
     * 班级信息最后更新时间
     */
    private LocalDateTime updateTime;

    // 构造方法、getter 和 setter 方法
}
