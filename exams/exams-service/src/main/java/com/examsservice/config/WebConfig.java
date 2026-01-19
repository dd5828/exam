package com.examsservice.config;

import com.common.json.JacksonObjectMapper;
import com.examsservice.interceptor.JwtTokenAdminInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 配置类，注册web层相关拦截器
 */
@RequiredArgsConstructor
@Configuration
@Slf4j
public class WebConfig extends WebMvcConfigurationSupport {

    private final JwtTokenAdminInterceptor jwtTokenAdminInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("开始注册自定义拦截器");
        // 对swagger的请求不进行拦截


        registry.addInterceptor(jwtTokenAdminInterceptor)
                .excludePathPatterns("/admin/login",
                        "/admin/login-a",
                        "/admin/register",
                        "/doc.html/**", // 对swagger的请求不进行拦截
                        "/webjars/**", // 对swagger的请求不进行拦截
                        "/swagger-resources/**", // 对swagger的请求不进行拦截
                        "/v3/**", // 对swagger的请求不进行拦截
                        "/captcha/**"//对获取图形验证码不拦截
                        );//登录和注册不拦截
    }

    /**
     * 设置静态资源映射
     * @param registry
     */
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /**
     * 扩展mvc框架的消息转换器
     * @param converters
     */

    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {    log.info("开始扩展消息转换器...");
        //创建一个消息转化器对象
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        //设置对象转换器，可以将Java对象转为json字符串
        converter.setObjectMapper(new JacksonObjectMapper());
        //将我们自己的转换器放入spring MVC框架的容器中
        converters.add(0,converter);
    }




}
