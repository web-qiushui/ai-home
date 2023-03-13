package com.home.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 数据字典
 * @author xiewei
 */
@Data
@ApiModel
public class DatadictVO {

	@ApiModelProperty("ID")
	private Integer id;

	@ApiModelProperty("字典类型")
	private String type;

	@ApiModelProperty("字典名")
	private String name;

	@ApiModelProperty("字典值")
	private String value;

	@ApiModelProperty("扩展1")
	private String n1;

	@ApiModelProperty("扩展2")
	private String n2;

	@ApiModelProperty("扩展3")
	private String n3;

	@ApiModelProperty("扩展4")
	private String n4;

	@ApiModelProperty("扩展5（JSON）")
	private String n5;

	@ApiModelProperty("状态   0：禁用  1：启用")
	private Integer status;
}
