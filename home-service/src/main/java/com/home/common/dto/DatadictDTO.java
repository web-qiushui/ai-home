package com.home.common.dto;

import com.home.common.utils.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 数据字典
 * @author xiewei
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
public class DatadictDTO extends PageReq {

	@ApiModelProperty("字典编码")
	@NotNull(message = "字典编码不能为空")
	private String type;

	@ApiModelProperty("字典名")
	@NotNull(message = "字典名不能为空")
	private String name;

	@ApiModelProperty("扩展1")
	private String n1;

	@ApiModelProperty("扩展4（1 上级 2 子集）")
	private String n4;

	@ApiModelProperty("状态   0：禁用  1：启用")
	private Integer status;

}
