package com.exam.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 分类视图对象
 */
@Data
@ApiModel("分类视图对象")
public class CategoryVO {
    @ApiModelProperty("主键")
    private Long id;
    @ApiModelProperty("分类名称")
    private String typeName;
    @ApiModelProperty("创建时间")
    private String createTimeStr; // 假设以字符串形式展示创建时间，方便前端处理
    @ApiModelProperty("更新时间")
    private String updateTimeStr;

}
