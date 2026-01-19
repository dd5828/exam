package com.examsservice;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Scanner;

@SpringBootTest
class ExamsServiceApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void getCaptcha() {
        //定义图形验证码的长和宽
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 100);
        //图形验证码写出，可以写出到文件，也可以写出到流
        lineCaptcha.write("g:/line.png");
        //输出code
        System.out.println("生成验证码:" + lineCaptcha.getCode());
    }



}
