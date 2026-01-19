package com.examsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement //开启注解方式的事务管理
public class ExamsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExamsServiceApplication.class, args);
    }

}
