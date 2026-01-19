package com.examsservice.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.common.constant.MessageConstant;
import com.common.result.PageResult;
import com.common.result.Result;
import com.common.utils.JwtUtil;
import com.exam.dto.StudentsPageQueryDTO;
import com.exam.dto.UserDTO;
import com.exam.dto.UserLoginDTO;
import com.exam.dto.UserUpdatePasswordDTO;
import com.exam.entity.User;
import com.exam.vo.UserLoginVO;
import com.exam.vo.UserVo;
import com.examsservice.config.Captcha;
import com.examsservice.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("admin")
@Api(tags = "教师相关接口")
@RestController
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final Captcha captcha;

    @PostMapping("/register")
    @ApiOperation("教师注册接口")
    public Result<String> insert(@RequestBody UserDTO userDTO){
        log.info("注册参数{}",userDTO);
        userService.insert(userDTO);
        return Result.success();
    }

    @PostMapping("/login")
    @ApiOperation("教师登录接口")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO, HttpSession session){
        log.info("登录参数{}",userLoginDTO);
        if (!captcha.checkCaptcha(userLoginDTO.getCaptcha(),session)){
            return Result.error(MessageConstant.Captcha_ERROR);

        }
        User user=userService.login(userLoginDTO);

        //登录成功后生成jwt令牌
        Map<String,Object> claims=new HashMap<>();
        claims.put("id",user.getId());
        claims.put("studentId",user.getStudentId());
        String token= JwtUtil.genToken(claims);
        log.info("token:{}",token);
        //把user封装为UserLoginVo
        UserLoginVO userLoginVO=UserLoginVO.builder()
                .id(user.getId())
                .studentId(user.getStudentId())
                .name(user.getName())
                .token(token)
                .build();

       return Result.success(userLoginVO);
    }


    @PostMapping("/login-a")
    @ApiOperation("不校验图形验证码的登录")
    public Result<UserLoginVO> logina(@RequestBody UserLoginDTO userLoginDTO){
        User user=userService.login(userLoginDTO);

        //登录成功后生成jwt令牌
        Map<String,Object> claims=new HashMap<>();
        claims.put("id",user.getId());
        claims.put("studentId",user.getStudentId());
        String token= JwtUtil.genToken(claims);
        log.info("token:{}",token);
        //把user封装为UserLoginVo
        UserLoginVO userLoginVO=UserLoginVO.builder()
                .id(user.getId())
                .studentId(user.getStudentId())
                .name(user.getName())
                .phone(user.getPhone())
                .token(token)
                .build();

        return Result.success(userLoginVO);
    }

    @PutMapping("/update")
    @ApiOperation("编辑管理员信息")
    public Result<String> update(@RequestBody UserDTO userDTO){
        log.info("编辑员工：{}",userDTO);
        userService.updateUser(userDTO);
        return Result.success();
    }

    @PutMapping("/upPassword")
    @ApiOperation("修改管理员密码")
    public Result<String> updatePassword(@RequestBody UserUpdatePasswordDTO UserUpdatePasswordDTO){
        log.info("编辑员工：{}",UserUpdatePasswordDTO);
        userService.updateUserPassword(UserUpdatePasswordDTO);
        return Result.success();
    }
}
