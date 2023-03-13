package com.home.modules.generator.vo;

import com.baomidou.mybatisplus.annotation.*;
import com.home.common.utils.BeanEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 顾问
 * 
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-11 21:12:12
 */
@Data
public class ConsultantVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("id")
	protected Integer id;

	@ApiModelProperty("创建时间")
	@TableField(value = "create_time",fill = FieldFill.INSERT)
	protected Date createTime;

	@ApiModelProperty("类型(1 客户顾问 2 置业顾问)")
	private Integer type;

	@ApiModelProperty("审核类型（1、申请 2、平台审核 3、企业审核）")
	private Integer auditType;

	@ApiModelProperty("状态（1 认证中 2 认证成功 3 认证失败）")
	private Integer status;

	@ApiModelProperty("星级(1-5)")
	private Integer star;

	@ApiModelProperty("咨询次数")
	private Integer advisoryCount;

	@ApiModelProperty("推荐置业次数")
	private Integer buyHomeCount;

	@ApiModelProperty("手机号")
	private String phone;

	@ApiModelProperty("身份证号")
	private String idCard;

	@ApiModelProperty("姓名")
	private String name;

	@ApiModelProperty("头像")
	private String avatar;

	@ApiModelProperty("项目名")
	private String projectName;

	@ApiModelProperty("昵称")
	private String nickName;

	@ApiModelProperty("头像")
	private String avatarUrl;

	@ApiModelProperty("所属项目")
	private Project project;

	@ApiModelProperty("企业Id")
	private Integer firmId;

	@ApiModelProperty("用户Id")
	private Integer userId;

	@Data
	public static class Project {

		@ApiModelProperty("项目名称")
		private String name;

		@ApiModelProperty("项目地址")
		private String address;

		@ApiModelProperty("项目图片")
		private String imagesUrl;

		@ApiModelProperty("项目标签")
		private List<String> label;
	}


}
