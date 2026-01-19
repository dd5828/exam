package com.exam.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 题目实体
 */
@Data
@Builder//允许使用构建器模式创建对象
@NoArgsConstructor//为类生成一个无参构造函数
@AllArgsConstructor//为类生成一个包含所有字段的构造函数
@ApiModel("题目实体")
public class Questions {
    @ApiModelProperty("题目主键 ID")
    private Long id;
    @ApiModelProperty("题目标题")
    private String title;
    @ApiModelProperty("题目标签")
    private String tags;
    @ApiModelProperty("题目所属分类 ID")
    private Long categoryId;
    @ApiModelProperty("题型")
    private String questionType;
    @ApiModelProperty("答题总次数")
    private Integer totalAnswerCount;
    @ApiModelProperty("错误答题次数")
    private Integer wrongAnswerCount;
    @ApiModelProperty("题目创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;
    @ApiModelProperty("题目更新时间")
    @TableField("update_time")
    private LocalDateTime updateTime;
    @ApiModelProperty("创建题目用户 ID")
    private Long createUserId;
    @ApiModelProperty("更新题目用户 ID")
    private Long updateUserId;
}
