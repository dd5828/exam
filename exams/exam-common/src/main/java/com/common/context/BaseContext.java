package com.common.context;

//往当前用户请求线程里存储，拿取，删除

import java.util.Date;

/**
 *
 * ThreadLocal 操作的工具类
 */
public class BaseContext {

     // 为 Long 类型的线程本地变量
    public static ThreadLocal<Long> threadLocal = new ThreadLocal<>();



    //往线程存储id
    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }


    /**
     * 从线程空间获取当前用户id
     * @return
     */
    public static Long getCurrentId() {
        return threadLocal.get();
    }

    public static void removeCurrentId() {
        threadLocal.remove();
    }



}
