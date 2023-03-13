package com.home.modules.generator.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
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
public class SaveCarouselVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("主键")
	private Integer id;

	@ApiModelProperty("轮播图")
	@NotNull(message = "轮播图不能为空")
	private String imageUrl;

	@ApiModelProperty("描述")
	private String description;

	@ApiModelProperty("排序")
	private Integer sort;

	@ApiModelProperty("状态（0 禁用 1 启用）")
	private Integer status;

	@ApiModelProperty("页面路径")
	private String pagePath;

}
