package com.home.common.aspect;

import com.google.common.cache.Cache;
import com.home.common.annotation.NoRepeatSubmit;
import com.home.common.exception.RRException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 重复提交注解
 * @author xiewei
 */
@Slf4j
@Aspect
@Component
public class NoRepeatSubmitAspect {

    @Resource
    private Cache<String, Integer> cache;

    @Before("@annotation(nrs)")
    public void doNoRepeatSubmitBefore(JoinPoint joinPoint, NoRepeatSubmit nrs) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String sessionId = Objects.requireNonNull(RequestContextHolder.getRequestAttributes()).getSessionId();
        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();
        String key = sessionId + "-" + request.getServletPath();
        // 如果缓存中有这个url视为重复提交
        if (cache.getIfPresent(key) != null) {
            log.error("重复提交");
            throw new RRException("请勿重复提交", 10001);
        }
        cache.put(key, 0);
    }
}
