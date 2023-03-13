package com.home.modules.app.annotation;

import java.lang.annotation.*;

/**
 * app登录效验
 *
 * @author xw
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Login {
}
