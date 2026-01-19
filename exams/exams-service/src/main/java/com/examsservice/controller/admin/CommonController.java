package com.examsservice.controller.admin;

import com.common.constant.MessageConstant;
import com.common.context.BaseContext;
import com.common.exception.AccountNotFoundException;
import com.common.result.Result;
import com.common.utils.AliOssUtil;
import com.examsservice.config.Captcha;
import com.examsservice.properties.AliOssProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.UUID;


@RequestMapping("/common")
@RestController
@Slf4j
@RequiredArgsConstructor
@Api(tags = "通用相关文档")
public class CommonController {
    private final Captcha captcha;
    private final AliOssUtil aliOssUtil;
    /**
     * 生成验证码
     * @param session
     * @param response
     */
    @GetMapping("/getCaptcha")
    @ApiOperation("生成验证码")
    public void getCaptcha(HttpSession session, HttpServletResponse response) {
        captcha.getCaptcha(session,response);
    }


    /**
     * 检验图形验证码
     * @param key
     * @param session
     * @return
     */
    @PostMapping("/check")
    @ApiOperation("校验验证码")
    public  Result<String> checkCaptcha(@RequestBody String key, HttpSession session) {

        boolean isValid = captcha.checkCaptcha(key, session);
        if (isValid) {
            return Result.success();
        } else {
            return Result.error(MessageConstant.Captcha_ERROR);
        }
    }


    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    @ApiOperation("文件上传接口")
    public Result<String> upload(MultipartFile file){
        log.info("文件上传：{}",file);
        // 获取原始文件名和文件扩展名
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

        // 生成新的文件名，包括随机UUID和文件扩展名
        String fileName = UUID.randomUUID().toString() + extension;

        try {
            // 调用阿里云工具类上传文件，并获取上传后的文件路径
            String filePath = aliOssUtil.upload(file.getBytes(), fileName);

            // 返回成功结果，包含文件的访问路径
            return Result.success(filePath);
        } catch (IOException e) {
            // 捕获上传过程中的IO异常，记录错误日志
            log.error("文件上传失败: {}", e.getMessage());
            return Result.error(MessageConstant.UPLOAD_FAILED);
        }


//        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}
