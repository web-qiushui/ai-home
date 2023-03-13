package com.home.modules.generator.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 测评表
 * 
 * @author yaokai
 * @email 906147469@qq.com
 * @date 2020-06-11 21:12:13
 */
@Data
@Builder
@ApiModel
@AllArgsConstructor
@NoArgsConstructor
public class SaveEvaluationDTO implements Serializable {

	@ApiModelProperty(value = "测评Id")
	private Integer id;

	@ApiModelProperty(value = "状态 0 隐藏 1 显示")
	private Integer status;

	@NotNull(message = "类型不能为空")
	@ApiModelProperty(value = "类型 1 测评报告 2 快讯")
	private Integer type;

	@ApiModelProperty("区域")
	private String region;

	@ApiModelProperty("价格")
	private String price;

	@ApiModelProperty("面积")
	private String area;

	@ApiModelProperty("标签")
	private List<String> label;

	@NotNull(message = "内容不能为空")
	@ApiModelProperty("内容")
	private String content;

	@ApiModelProperty("描述")
	private String description;

	@ApiModelProperty("标题")
	private String title;

	@ApiModelProperty("作者")
	private String author;

	@ApiModelProperty("原创")
	private String original;

	@ApiModelProperty("公众号名称")
	private String thePublicName;

	@ApiModelProperty("主题图")
	private String mainImage;

	@ApiModelProperty(value = "管理员Id", hidden = true)
	private Integer sysUserId;

	@ApiModelProperty(value = "用户Id", hidden = true)
	private Integer userId;

}
