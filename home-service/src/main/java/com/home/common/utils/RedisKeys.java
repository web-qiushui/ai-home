package com.home.common.utils;

/**
 * Redis所有Keys
 *
 * @author xw
 */
public class RedisKeys {

    public static String getSysConfigKey(String key){
        return "sys:config:" + key;
    }
}
