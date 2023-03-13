package com.home.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 数据字典
 * @author xiewei
 */
@Data
@ApiModel
public class CaptchaDTO {


	@Pattern(regexp = "^[1][0-9]{10}$",message = "手机号格式不正确")
	@NotNull(message = "手机号不能为空")
	private String phone;

	@ApiModelProperty(value = "随机uuid", required = true)
	@NotNull(message = "uuid不能为空")
	private String uuid;

	@ApiModelProperty("验证类型（0 通用 1 注册 2 登录 3 找回密码）")
	private Integer type;

	@ApiModelProperty("验证码")
	private String code;

}
