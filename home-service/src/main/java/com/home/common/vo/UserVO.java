package com.home.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 用户
 * @author xiewei
 */
@Data
@ApiModel
public class UserVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("用户Id")
	private Integer id;

	@ApiModelProperty("创建时间")
	private Date createTime;

	@ApiModelProperty("用户名")
	private String userName;

	@ApiModelProperty("手机号")
	private String phone;

	@ApiModelProperty("身份证")
	private String idCard;

	@ApiModelProperty("姓名")
	private String name;

	@ApiModelProperty("昵称")
	private String nickName;

	@ApiModelProperty("性别（0：未知，1：男，2：女）")
	private Integer gender;

	@ApiModelProperty("头像")
	private String avatarUrl;

	@ApiModelProperty("地址")
	private String address;

	@ApiModelProperty("角色（0 用户 1 AI顾问 2 置业顾问）")
	private Integer role;

	@ApiModelProperty("认证状态（0 未认证 1 认证中 2 认证成功  3 认证失败）")
	private Integer authenticateStatus;

	@ApiModelProperty("状态（0 锁定 1 正常）")
	private Integer status;

}
