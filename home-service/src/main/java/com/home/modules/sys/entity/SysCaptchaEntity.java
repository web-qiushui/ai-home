package com.home.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 系统验证码
 *
 * @author xw
 */
@Data
@TableName("sys_captcha")
public class SysCaptchaEntity {
    @TableId(type = IdType.INPUT)
    private String uuid;
    /**
     * 验证码
     */
    private String code;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 验证次数
     */
    private Integer num;
    /**
     * 验证类型（1 登录 2 注册 3 找回密码）
     */
    private Integer type;
    /**
     * 过期时间
     */
    private Date expireTime;

}
