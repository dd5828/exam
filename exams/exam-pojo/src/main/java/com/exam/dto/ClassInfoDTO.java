package com.exam.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 班级信息DTO
 */
@Data

public class ClassInfoDTO {
    /**
     * 班级的唯一标识 ID
     */
    @ApiModelProperty("主键值")
    private Long id;
    /**
     * 班级名称
     */
    @ApiModelProperty("班级名")
    private String className;
    /**
     * 专业
     */
    @ApiModelProperty("专业")
    private String major;

}
