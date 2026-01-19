package com.examsservice.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.common.result.PageResult;
import com.exam.dto.StudentsPageQueryDTO;
import com.exam.dto.UserDTO;
import com.exam.dto.UserLoginDTO;
import com.exam.dto.UserUpdatePasswordDTO;
import com.exam.entity.User;
import com.exam.vo.UserVo;

public interface UserService {

    /**
     * 用户注册
     * @param userDTO
     */
    void insert(UserDTO userDTO);

    /**
     * 用户登录
     *
     * @param userLoginDTO
     * @return
     */
    User login(UserLoginDTO userLoginDTO);


    /**
     * 修改管理员信息
     * @param userDTO
     */
    void updateUser(UserDTO userDTO);

    /**
     * 修改管理员密码
     * @param userUpdatePasswordDTO
     */
    void updateUserPassword(UserUpdatePasswordDTO userUpdatePasswordDTO);
}
