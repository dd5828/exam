package com.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;
import java.util.Map;

/**
 * jwt的生成与校验方法
 */
public class JwtUtil {

    //jWT 的签名密钥
    private static final String KEY = "hahaha";

	
	//接收业务数据,生成token并返回
    public static String genToken(Map<String, Object> claims) {
        return JWT.create()//创建一个新的 JWT 构建器
                .withClaim("claims", claims)//将 claims 添加到 JWT 中，以 "claims" 作为声明的键。
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 2))//设置 JWT 的过期时间，这里设置为当前时间加上 2 小时
                .sign(Algorithm.HMAC256(KEY));//返回: 返回生成的 JWT 令牌字符串
    }

    public static Map<String, Object> parseToken(String token) {
        return JWT.require(Algorithm.HMAC256(KEY))//创建一个验证器，指定使用 HMAC-SHA256 算法和密钥来验证 JWT
                .build()//构建验证器
                .verify(token)//验证并解码 JWT 令牌
                .getClaim("claims")//提取 JWT 中名为 "claims" 的声明部分
                .asMap();//返回: 返回包含 JWT 声明数据的 Map 对象
    }

}
