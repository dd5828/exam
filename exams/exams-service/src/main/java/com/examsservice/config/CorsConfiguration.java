package com.examsservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 解决跨域问题
 */

@Configuration
public class CorsConfiguration implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //设置路径·的映射
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                //容许哪些请求访问
                .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS")
                //是否容许携带Cookie
                .allowCredentials(true)
                //设置访问一次后。3600秒以后不再询问是否放行
                .maxAge(3600)
                //请求头的通配符
                .allowedHeaders("*");

    }
}
