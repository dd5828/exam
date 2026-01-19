package com.exam.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 查询学生返回VO
 */
@Data
@Builder//允许使用构建器模式创建对象
@NoArgsConstructor//为类生成一个无参构造函数
@AllArgsConstructor//为类生成一个包含所有字段的构造函数
@ApiModel(description = "学生查询VO")
public class StudentInfoVo {
    /**
     * 学生的唯一标识 ID
     */
    @ApiModelProperty("学生的唯一标识 ID")
    private int studentId;
    /**
     * 学生姓名
     */
    @ApiModelProperty("学生姓名")
    private String studentName;
    /**
     * 学生学号
     */
    @ApiModelProperty("学生学号")
    private String studentNumber;

//    /**
//     * 学生所属班级的 ID
//     */
//    @ApiModelProperty("学生所属班级的id")
//    private int classId;

    /**
     * 班级名称
     */
    @ApiModelProperty("班级名称")
    private String className;
    /**
     * 专业
     */
    @ApiModelProperty("专业")
    private String major;

    /**
     * 学生信息创建时间
     */
    @ApiModelProperty("学生信息创建时间")
    private LocalDateTime createTime;
    /**
     * 学生信息最后更新时间
     */
    @ApiModelProperty("学生信息最后更新时间")
    private LocalDateTime updateTime;
}
