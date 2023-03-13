package com.home.common.dto;

import com.home.common.utils.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 系统单页面表
 * 
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-28 22:14:45
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
public class SinglePageDTO extends PageReq implements Serializable {
	private static final long serialVersionUID = 1L;


	@ApiModelProperty("类型")
	private Integer type;

	@ApiModelProperty("编码")
	private String code;

	@ApiModelProperty("标题")
	private String title;

	@ApiModelProperty("状态（0 禁用 1 启用）")
	private Integer status;

}
