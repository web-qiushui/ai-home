package com.home.modules.generator.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.home.common.utils.BeanEntity;
import lombok.*;

/**
 * 用户登录记录表
 * 
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-07-22 22:25:48
 */
@Data
@TableName("home_login")
@EqualsAndHashCode(callSuper = true)
public class LoginEntity extends BeanEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 用户Id
	 */
	private Integer homeUserId;
	/**
	 * Ip
	 */
	private String ip;

}
