package com.home.modules.generator.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 开盘预告表
 * 
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-22 20:41:42
 */
@Data
public class OpeningNoticeVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("主键")
	private Integer id;

	@ApiModelProperty(value = "创建时间", example = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	@ApiModelProperty("描述")
	private String description;

	@ApiModelProperty("图片")
	private String imageUrl;

	@ApiModelProperty("状态（0 禁用 1 启用）")
	private Integer status;

}
