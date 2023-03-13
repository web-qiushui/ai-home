package com.home.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xw
 * @date 2020/8/3 15:04
 */
@Configuration
public class HttpMessageConvertersConfig {

    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters(){
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(
                /*// List类型字段为null时输出[]而非null
                SerializerFeature.WriteNullListAsEmpty,
                // 字符串类型字段为null时间输出""而非null
                SerializerFeature.WriteNullStringAsEmpty,
                // 数值字段如果为null,输出为0,而非null
                SerializerFeature.WriteNullNumberAsZero,
                // Boolean字段如果为null,输出为false,而非null
                SerializerFeature.WriteNullBooleanAsFalse,*/
                // 美化json输出，否则会作为整行输出
                SerializerFeature.PrettyFormat,
                // 禁用循环引用检测
                SerializerFeature.DisableCircularReferenceDetect,
                // 显示空字段
                SerializerFeature.WriteMapNullValue,
                // 时间格式yyyy-MM-dd HH: mm: ss
                SerializerFeature.WriteDateUseDateFormat);
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        fastJsonHttpMessageConverter.setSupportedMediaTypes(fastMediaTypes);
        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
        return new HttpMessageConverters(fastJsonHttpMessageConverter);
    }
}