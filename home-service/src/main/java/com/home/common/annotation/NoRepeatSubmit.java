package com.home.common.annotation;

import java.lang.annotation.*;

/**
 * 重复提交注解（作用到方法上，运行时有效）
 * @author xiewei
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoRepeatSubmit {
}
