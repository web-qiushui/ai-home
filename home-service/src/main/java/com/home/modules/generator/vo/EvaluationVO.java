package com.home.modules.generator.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
@ApiModel
public class EvaluationVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("主键TableId")
	private Integer id;

	@ApiModelProperty("创建时间")
	private Date createTime;

	@ApiModelProperty("内容")
	private String content;

	@ApiModelProperty("标题")
	private String title;

	@ApiModelProperty("作者")
	private String author;

	@ApiModelProperty("原创")
	private String original;

	@ApiModelProperty("公众号名称")
	private String thePublicName;

	@ApiModelProperty("描述")
	private String description;

	@ApiModelProperty("点赞数")
	private Integer likeCount;

	@ApiModelProperty("评论数")
	private Integer commentCount;

	@ApiModelProperty("收藏数")
	private Integer collectCount;

	@ApiModelProperty("转发数")
	private Integer forwardCount;

	@ApiModelProperty("标签")
	private List<String> label;

	@ApiModelProperty(value = "标签字符串", hidden = true)
	private String labelStr;

	@ApiModelProperty("类型 1 测评报告 2 快讯")
	private Integer type;

	@ApiModelProperty("主题图")
	private String mainImage;

	@ApiModelProperty("区域")
	private String region;

	@ApiModelProperty("价格")
	private String price;

	@ApiModelProperty("面积")
	private String area;

	@ApiModelProperty("是否点赞（0 否 1 是）")
	private Integer isLike;

	@ApiModelProperty("是否收藏（0 否 1 是）")
	private Integer isCollect;

	@ApiModelProperty(value = "状态 0 隐藏 1 显示")
	private Integer status;

}
