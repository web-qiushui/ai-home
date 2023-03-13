package com.home.common.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

/**
 * @author xiewei
 * 用于封装异步调用返回结果
 */
@Data
@ApiModel
public class JsonResult<T> {

    private static final String COMMON_ERROR_MESSAGE = "网络繁忙，请稍后重试";
    public static final String SUCCESS = "success";
    public static final String FAIL = "fail";
    public static final int SUCCESS_CODE = 200;
    public static final int FAIL_CODE = 500;

    @ApiModelProperty("调用是否成功标识，0或200：成功，500:失败, 403:非法调用")
    private int code;

    @ApiModelProperty("调用结果")
    private T data;

    @ApiModelProperty("结果消息，如果调用成功，消息通常为空")
    private String msg;
    
    @ApiModelProperty("补充信息")
    private Object desc;

    private JsonResult() {
    }

    /**
     * @param code 代码
     * @param data 结果
     * @param msg  消息
     */
    public JsonResult(int code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    /**
     * @param code 代码
     * @param data 结果
     * @param msg  消息
     * @param desc 补充信息
     */
    public JsonResult(int code, T data, String msg, Object desc) {
        this.code = code;
        this.data = data;
        this.msg = msg;
        this.desc = desc;
    }

    /**
     * @param code 代码
     * @param msg  消息
     */
    public JsonResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 请求成功消息
     *
     * @param data
     * @return
     */
    public static <E> JsonResult<E> success(E data) {
        return new JsonResult<E>(SUCCESS_CODE, data, SUCCESS);
    }

    /**
     * 请求成功消息
     *
     * @param data
     * @return
     */
    public static <E> JsonResult<E> success(int code, E data) {
        return new JsonResult<E>(code, data, SUCCESS);
    }

    /**
     * 请求成功消息
     *
     * @return
     */
    public static <E> JsonResult<E> success() {
        return new JsonResult<E>(SUCCESS_CODE, null, SUCCESS);
    }


    /**
     * 请求成功方法 ，data返回值，msg提示信息
     *
     * @param data
     * @param msg
     * @return
     */
    public static <E> JsonResult<E> success(E data, String msg) {
        return new JsonResult<E>(SUCCESS_CODE, data, msg);
    }

    /**
     * 请求成功方法 ，data返回值，desc补充信息
     *
     * @param data
     * @return
     */
    public static <E> JsonResult<E> success(Object desc, E data) {
        return new JsonResult<E>(SUCCESS_CODE, data, SUCCESS, desc);
    }

    /**
     * 请求失败消息
     *
     * @param msg
     * @return
     */
    public static <E> JsonResult<E> fail(String msg) {
        return new JsonResult<>(FAIL_CODE, null, StringUtils.isEmpty(msg) ? COMMON_ERROR_MESSAGE : msg);
    }

    /**
     * 请求失败消息
     *
     * @param msg
     * @return
     */
    public static <E> JsonResult<E> fail(Integer code, String msg) {
        return new JsonResult<>(code, null, StringUtils.isEmpty(msg) ? COMMON_ERROR_MESSAGE : msg);
    }

    /**
     * 请求失败消息
     *
     * @param msg
     * @return
     */
    public static <E> JsonResult<E> fail(Integer code, String msg, Object desc) {
        return new JsonResult<>(code, null, StringUtils.isEmpty(msg) ? COMMON_ERROR_MESSAGE : msg, desc);
    }

    /**
     * 请求失败消息
     *
     * @param msg
     * @return
     */
    public static <E> JsonResult<E> fail(Integer code, E data, String msg) {
        return new JsonResult<>(code, data, StringUtils.isEmpty(msg) ? COMMON_ERROR_MESSAGE : msg);
    }

    /**
     * 逻辑处理是否成功
     *
     * @return 是否成功
     */
    @JsonIgnore()
    public boolean isSuccess() {
        return this.code == 0 || this.code == 200;
    }
}