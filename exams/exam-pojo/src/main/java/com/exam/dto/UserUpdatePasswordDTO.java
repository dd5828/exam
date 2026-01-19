package com.exam.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 修改密码DTO
 */
@Data
@ApiModel(description = "用户修改DTO")
public class UserUpdatePasswordDTO {
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("当前密码")
    private String oldPassword;

    @ApiModelProperty("新密码")
    private String newPassword;

    @ApiModelProperty("确认密码")
    private String confirmNewPassword;
}
