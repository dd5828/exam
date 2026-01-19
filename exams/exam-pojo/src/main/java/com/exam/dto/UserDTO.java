package com.exam.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 用户注册所需参数
 */
@Data
public class UserDTO {

    @ApiModelProperty("主键值")
    private Long id;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("学号")
    private String studentId;
    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("用户头像")
    private String img;

    @ApiModelProperty("手机号")
    private String phone;




}
