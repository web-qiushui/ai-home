package com.home.modules.generator.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 我的页面数据
 * 
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-22 20:41:42
 */
@Data
public class MineInfoVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("购房需求数量")
	private Integer demandCount;

	@ApiModelProperty("我的顾问数量")
	private Integer consultantCount;

	@ApiModelProperty("我的分享数量")
	private Integer evaluationForwardCount;

	@ApiModelProperty("我的评论数量")
	private Integer evaluationCommentCount;

	@ApiModelProperty("我的评价数量")
	private Integer consultantCommentCount;

}
