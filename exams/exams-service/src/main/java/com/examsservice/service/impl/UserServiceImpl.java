package com.examsservice.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.constant.MessageConstant;
import com.common.exception.AccountNotFoundException;

import com.exam.dto.UserDTO;
import com.exam.dto.UserLoginDTO;
import com.exam.dto.UserUpdatePasswordDTO;
import com.exam.entity.User;

import com.examsservice.mapper.UserMapper;
import com.examsservice.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final PasswordEncoder passwordEncoder;//加密

    private final UserMapper userMapper;


    /**
     * 注册
     * @param userDTO
     */
    @Override
    public void insert(UserDTO userDTO) {

        //判断学号是否已经存在
        LambdaQueryWrapper<User> lambdaQueryWrapper=new LambdaQueryWrapper<>();
       lambdaQueryWrapper.eq(User::getStudentId,userDTO.getStudentId());
       if(userMapper.selectOne(lambdaQueryWrapper)!=null){
           throw new AccountNotFoundException(MessageConstant.ACCOUNT_EXISTS);
       }

        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));//对密码进行加密

        User user= BeanUtil.copyProperties(userDTO,User.class);
        user.setCreateTime(LocalDateTime.now());//设置时间
        log.info("user:{}",user);
        userMapper.insert(user);
    }

    /**
     * 用户登录
     *
     * @param userLoginDTO
     * @return
     */
    @Override
    public User login(UserLoginDTO userLoginDTO) {

        //1.获取学号
        String studentId=userLoginDTO.getStudentId();
        String password=userLoginDTO.getPassword();
        //2.根据用户名查询数据库中的额数据
        //2.1创建一个 LambdaQueryWrapper 对象，用于构造查询条件
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //2.2使用 eq 方法添加查询条件，User::getStudentId 是实体类中的 getStudentId 方法的引用，studentId 是要匹配的值。
        // 这个条件表示查询 studentId 等于指定值的记录。
        lambdaQueryWrapper.eq(User::getStudentId, studentId);
        // 执行查询
        //2.3selectOne 方法会返回符合条件的单条记录。如果有多条记录，selectOne 会抛出异常，确保条件唯一性。如果没有记录，则返回 null。
        User user = userMapper.selectOne(lambdaQueryWrapper);

        //2.4处理异常（用户不存在）
        if(user==null){
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        //3，密码比对
        if(!passwordEncoder.matches(password, user.getPassword())){
            //密码错误
            throw new AccountNotFoundException(MessageConstant.PASSWORD_ERROR);
        }

        return user;
    }



    /**
     * 修改信息
     * @param userDTO
     */
    @Override
    public void updateUser(UserDTO userDTO) {
        //使用 Lambda 表达式构建更新操作
        lambdaUpdate()
                .set(userDTO.getName()!=null,User::getName,userDTO.getName())
                .set(userDTO.getStudentId()!=null,User::getStudentId,userDTO.getStudentId())
                .set(userDTO.getPhone()!=null,User::getPhone,userDTO.getPhone())
                .set(userDTO.getImg()!=null,User::getImg,userDTO.getImg())
                .eq(User::getId,userDTO.getId())
                .update();
    }

    /**
     * 修改管理员密码
     * @param userUpdatePasswordDTO
     */
    @Override
    public void updateUserPassword(UserUpdatePasswordDTO userUpdatePasswordDTO) {
        User user = userMapper.selectById(userUpdatePasswordDTO.getId());
        //密码比对
        if(!passwordEncoder.matches(userUpdatePasswordDTO.getOldPassword(), user.getPassword())){
            //密码错误
            throw new AccountNotFoundException(MessageConstant.PASSWORD_ERROR);
        }

        //使用 Lambda 表达式构建更新操作
        lambdaUpdate()
                .set(userUpdatePasswordDTO.getConfirmNewPassword()!=null,User::getPassword,passwordEncoder.encode(userUpdatePasswordDTO.getNewPassword()))
                .eq(User::getId,userUpdatePasswordDTO.getId())
                .update();

    }
}
