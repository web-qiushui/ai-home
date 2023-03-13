package com.home.modules.generator.dto;

import com.home.common.utils.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
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
@EqualsAndHashCode(callSuper = true)
public class EvaluationDTO extends PageReq implements Serializable {

	@ApiModelProperty(value = "测评Id", hidden = true)
	private Integer id;

	@ApiModelProperty(value = "状态 0 隐藏 1 显示")
	private Integer status;

	@ApiModelProperty(value = "类型 1 测评报告 2 快讯")
	private Integer type;

	@ApiModelProperty("搜索")
	private String search;

	@ApiModelProperty("区域")
	private String region;

	@ApiModelProperty("价格")
	private String price;

	@ApiModelProperty("面积")
	private String area;

	@ApiModelProperty("标签")
	private List<String> label;

	@ApiModelProperty(value = "用户Id", hidden = true)
	private Integer userId;

	@ApiModelProperty(value = "管理员用户Id", hidden = true)
	private Integer sysUserId;

}
