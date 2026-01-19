package com.exam.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
;

/**
 * 学生分页查询dto
 */
@Data
@ApiModel(description = "用户查询条件实体")
public class StudentsPageQueryDTO{

    @ApiModelProperty("姓名关键字")
    private String studentName;

    @ApiModelProperty("学号关键字")
    private String studentNumber;
    //页码
    private int page;

    //每页显示记录数
    private int pageSize;

}
