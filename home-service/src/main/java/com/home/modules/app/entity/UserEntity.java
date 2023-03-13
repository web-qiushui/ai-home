package com.home.modules.app.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.home.common.utils.BeanEntity;
import lombok.*;

import java.io.Serializable;


/**
 * 用户
 *
 * @author xw
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("home_user")
public class UserEntity extends BeanEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 是否删除 0 是 1 否
	 */
	@TableField(value = "is_delete",fill = FieldFill.INSERT)
	private Integer isDelete;
	/**
	 * 用户名
	 */
	private String userName;
	/**
	 * 密码
	 */
	private String passWord;
	/**
	 * 手机号
	 */
	private String phone;
	/**
	 * 身份证
	 */
	private String idCard;
	/**
	 * 微信openId
	 */
	private String openId;
	/**
	 * 微信unionId
	 */
	private String unionId;
	/**
	 * 微信sessionKey
	 */
	private String sessionKey;
	/**
	 * 姓名
	 */
	private String name;
	/**
	 * 昵称
	 */
	private String nickName;
	/**
	 * 性别（0：未知，1：男，2：女）
	 */
	private Integer gender;
	/**
	 * 头像
	 */
	private String avatarUrl;
	/**
	 * 地址
	 */
	private String address;
	/**
	 * 状态（0 锁定 1 正常）
	 */
	private Integer status;
	/**
	 * 角色（0 用户 1 AI顾问 2 置业顾问）
	 */
	private Integer role;

}
