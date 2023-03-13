package com.home.modules.generator.dto;

import com.home.common.utils.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 轮播图表
 * 
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-22 20:41:42
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
public class CarouselDTO extends PageReq implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("状态（0 禁用 1 启用）")
	private Integer status;

}
