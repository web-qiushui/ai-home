package com.home.common.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 系统单页面表
 * 
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-28 22:14:45
 */
@Data
@ApiModel
public class SinglePageVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("ID")
	private Integer id;

	@ApiModelProperty(value = "创建时间", example = "yyyy-MM-dd HH:mm:dd")
	private Date createTime;

	@ApiModelProperty("类型")
	private Integer type;

	@NotNull(message = "编码不能为空")
	@ApiModelProperty("编码")
	private String code;

	@NotNull(message = "标题不能为空")
	@ApiModelProperty("标题")
	private String title;

	@NotNull(message = "内容不能为空")
	@ApiModelProperty("内容")
	private String content;

	@ApiModelProperty("封面图")
	private String coverUrl;

	@ApiModelProperty("状态（0 禁用 1 启用）")
	private Integer status;

}
