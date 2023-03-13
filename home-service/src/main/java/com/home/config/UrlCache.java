package com.home.config;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author xiewei
 */
@Configuration
public class UrlCache {

    @Bean
    public Cache<String, Integer> getCache() {
        // 缓存有效期为2秒
        return CacheBuilder.newBuilder().expireAfterWrite(2L, TimeUnit.SECONDS).build();
    }
}
