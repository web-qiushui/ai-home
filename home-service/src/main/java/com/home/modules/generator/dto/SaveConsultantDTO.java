package com.home.modules.generator.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;

/**
 * 顾问表
 * 
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-11 21:12:12
 */
@Data
@Builder
@ApiModel
@AllArgsConstructor
@NoArgsConstructor
public class SaveConsultantDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("Id")
	protected Integer id;

	@ApiModelProperty("类型(1 客户顾问 2 置业顾问)")
	private Integer type;

	@Pattern(regexp = "^[1][0-9]{10}$",message = "手机号格式不正确")
	@ApiModelProperty("手机号")
	private String phone;

	@Pattern(regexp = "(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)", message = "身份证格式不正确")
	@ApiModelProperty("身份证号")
	private String idCard;

	@Pattern(regexp = "^[\\u4e00-\\u9fa5.·]{0,20}$", message = "姓名格式不正确")
	@ApiModelProperty("姓名")
	private String name;

	@NotNull(message = "头像不能为空")
	@ApiModelProperty("头像")
	private String avatar;

	@ApiModelProperty(value="用户Id", hidden = true)
	private Integer userId;

	@ApiModelProperty(value="企业Id", hidden = true)
	private Integer firmId;

	@Valid
	@ApiModelProperty("所属项目")
	private Project project;

	@NotNull(message = "验证码不能为空")
	@ApiModelProperty("验证码")
	private String code;

	@NotNull(message = "uuid不能为空")
	@ApiModelProperty("uuid")
	private String uuid;

	@Data
	public static class Project {

		@NotNull(message = "项目名称不能为空")
		@ApiModelProperty("项目名称")
		private String name;

		@NotNull(message = "项目地址不能为空")
		@ApiModelProperty("项目地址")
		private String address;

		@NotNull(message = "项目图片不能为空")
		@ApiModelProperty("项目图片")
		private String imagesUrl;

		@NotNull(message = "项目标签不能为空")
		@ApiModelProperty("项目标签")
		private List<String> label;
	}


}
