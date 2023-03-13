package com.home.modules.generator.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 轮播图表
 * 
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-22 20:41:42
 */
@Data
public class CarouselVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("主键")
	private Integer id;

	@ApiModelProperty(value = "创建时间", example = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	@ApiModelProperty("轮播图")
	private String imageUrl;

	@ApiModelProperty("描述")
	private String description;

	@ApiModelProperty("状态（0 禁用 1 启用）")
	private Integer status;

	@ApiModelProperty("排序")
	private Integer sort;

	@ApiModelProperty("页面路径")
	private String pagePath;

}
