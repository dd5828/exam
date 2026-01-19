package com.exam.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 分类数据传输对象
 */
@Data
@ApiModel("分类数据传输对象")
public class CategoryDTO {
    @ApiModelProperty("主键")
    private int id;
    @ApiModelProperty("分类名称")
    private String typeName;
    // 如果有其他需要在数据传输中用到的字段，可以在这里添加
}
