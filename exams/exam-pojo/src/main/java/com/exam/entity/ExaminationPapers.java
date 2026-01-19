package com.exam.entity;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 
 * </p>
 *
 * @author 何时去
 * @since 2024-09-21
 */

@Data
@Builder//允许使用构建器模式创建对象
@NoArgsConstructor//为类生成一个无参构造函数
@AllArgsConstructor//为类生成一个包含所有字段的构造函数
@ApiModel(value="ExaminationPapers对象", description="")
public class ExaminationPapers{



    @ApiModelProperty(value = "试卷编号，自增主键")
    @TableId(type = IdType.AUTO)
    private Integer paperId;

    @ApiModelProperty(value = "试卷标题")
    private String paperTitle;

    @ApiModelProperty(value = "试卷描述")
    private String description;

    @ApiModelProperty(value = "试卷分类id")
    private Long categoryId;

    @ApiModelProperty(value = "考试时长（分钟）")
    private Integer duration;

    @ApiModelProperty(value = "试卷总分")
    private Integer totalScore;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updatedAt;

    @ApiModelProperty(value = "试卷状态，1：启用，0：禁用")
    private Integer status;

    @ApiModelProperty(value = "考试开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "考试结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "创建用户id")
    private Long createUserId;

    @ApiModelProperty(value = "修改用户id")
    private Long updateUserId;

    @ApiModelProperty(value = "试卷图片")
    private String paperPictures;


}
