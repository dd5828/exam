package com.exam.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户vo实体
 */
@Data
@Builder//允许使用构建器模式创建对象
@NoArgsConstructor//为类生成一个无参构造函数
@AllArgsConstructor//为类生成一个包含所有字段的构造函数
@ApiModel(description = "用户VO实体")
public class UserLoginVO {

    @ApiModelProperty("主键值")
    private Long id;

    @ApiModelProperty("学号")
    private String studentId;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("姓名")
    private String phone;

    @ApiModelProperty("用户头像")
    private String img;

    @ApiModelProperty("jwt令牌")
    private String token;

}
