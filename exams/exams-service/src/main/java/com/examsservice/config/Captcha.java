package com.examsservice.config;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.common.constant.Constants;

import com.common.context.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;

@Component
@Slf4j
public class Captcha {


    /**
     * 生成图形验证码
     * @param response
     */
    public void getCaptcha(HttpSession session,HttpServletResponse response) {

        // 定义图形验证码的长和宽
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(Constants.CAPTCHA_WIDTH, Constants.CAPTCHA_HEIGHT);
        // 设置返回数据类型
        response.setContentType("image/jpeg");
        // 禁止使用缓存
        response.setHeader("Pragma", "No-cache");
        try {

            // 输出到页面
//            lineCaptcha.write(response.getOutputStream());
//            图形验证码写出，可以写出到文件，也可以写出到流
            lineCaptcha.write("G:\\vue_project\\exams\\src\\assets\\line.png");
            // 将 生成的验证码 和 验证码生成时间 存储到session中
            //将生成的验证码存储到 HttpSession 中，使用 CAPTCHA_KEY 作为键。这样在后续的请求处理中，
            // 可以从 HttpSession 中获取这个验证码进行验证。
            session.setAttribute(Constants.CAPTCHA_KEY, lineCaptcha.getCode());
            session.setAttribute(Constants.CAPTCHA_DATE, new Date());
            log.info("图形验证码：{}",lineCaptcha.getCode());
            // 关闭流
            response.getOutputStream().close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 校验图形验证码
     * @param captcha
     * @return
     */
    public Boolean checkCaptcha(String captcha, HttpSession session) {
        log.info("接收到验证码: {}", captcha);

        // 参数校验
        if (!StringUtils.hasLength(captcha)) {
            return false;
        }

        // 获取存储的验证码和生成时间
        String code = (String) session.getAttribute(Constants.CAPTCHA_KEY);
        Date createTime = (Date) session.getAttribute(Constants.CAPTCHA_DATE);

        // 判断验证码是否正确(验证码一般忽略大小写)
        if (captcha.equalsIgnoreCase(code)) {
            // 判断验证码是否过时
            if (createTime == null || System.currentTimeMillis() - createTime.getTime() > Constants.EXPIRATION_TIME) {
                return false;
            }
            log.info("成功");
            return true;
        }

        return false;
    }
}
