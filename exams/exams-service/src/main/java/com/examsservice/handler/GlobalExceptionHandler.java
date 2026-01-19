package com.examsservice.handler;


import com.common.constant.MessageConstant;
import com.common.exception.BaseException;
import com.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常，捕获父类异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /** * 捕获sql异常 * @param ex * @return *
     *
     */
    @ExceptionHandler public Result exceptionHandler(SQLIntegrityConstraintViolationException ex) {
        log.error("异常信息：{}", ex.getMessage());
        String message = ex.getMessage();
        //判断用户名是否已经存在，如果已经存在的抛出异常
        if(message.contains("Duplicate entry")){//报错信息是否包含"Duplicate entry"
            String[] split = message.split(" ");//空格分割
            String name = split[2];//分离出报错已经存在的用户名
            return Result.error(name + MessageConstant.ACCOUNT_EXISTS);}//返回结果用户名已存在
        return Result.error(MessageConstant.UNKNOWN_ERROR);}

}
