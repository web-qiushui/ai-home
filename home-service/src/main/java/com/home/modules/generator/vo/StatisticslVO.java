package com.home.modules.generator.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 报表统计
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-22 20:41:42
 */
@Data
public class StatisticslVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("在线数")
	private Integer onlineCount;

	@ApiModelProperty("转发数")
	private Integer forwardCount;

	@ApiModelProperty("点赞数")
	private Integer likeCount;

	@ApiModelProperty("评论数")
	private Integer commentCount;

	@ApiModelProperty("收藏数")
	private Integer collectCount;

	@ApiModelProperty("购房需求数")
	private Integer demandCount;

	@ApiModelProperty("测评数")
	private Integer evaluationCount;

	@ApiModelProperty("快讯数")
	private Integer newsCount;

}
