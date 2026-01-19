package com.exam.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 用户登录DTO
 */
@Data
@ApiModel(description = "用户登录DTO")
public class UserLoginDTO{

    @ApiModelProperty("学号")
    private String studentId;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("图形验证码")
    private String Captcha;
}
