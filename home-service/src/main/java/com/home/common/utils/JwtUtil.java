package com.home.common.utils;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 获取token中的userId
 */
@Slf4j
@Component
public class JwtUtil {


    /**
     * 获取token中的userId
     *
     * @return userId
     */
    public static Integer getUserId() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            String token = request.getHeader("token");
            String secondSection = token.split("\\.")[1];
            String body = new String(Base64.getDecoder().decode(secondSection), StandardCharsets.UTF_8);
            return JSON.parseObject(body).getInteger("sub");
        } catch (Exception e) {
            log.error("token解析异常");
            return null;
        }

    }

    /**
     * 获取token中的userId
     *
     * @return userId
     */
    public static Integer getUserId(String token) {
        try {
            String secondSection = token.split("\\.")[1];
            String body = new String(Base64.getDecoder().decode(secondSection), StandardCharsets.UTF_8);
            return JSON.parseObject(body).getInteger("sub");
        } catch (Exception e) {
            log.error("token解析异常");
            return null;
        }

    }

    /**
     * 判断token是否存在
     *
     * @return userId
     */
    public static Boolean isToken() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            String token = request.getHeader("token");
            return StringUtils.isNotEmpty(token);
        } catch (Exception e) {
            return false;
        }
    }
}
