package com.home.modules.generator.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 测评评论表
 * 
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-11 21:12:12
 */
@Data
@ApiModel
public class EvaluationCommentVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("id")
	protected Integer id;

	@ApiModelProperty(value = "创建时间", example = "yyyy-MM-dd HH:mm:ss")
	protected Date createTime;

	@ApiModelProperty("测评表Id")
	private Integer evaluationId;

	@ApiModelProperty("用户Id")
	private Integer userId;

	@ApiModelProperty("昵称")
	private String nickName;

	@ApiModelProperty("头像")
	private String avatarUrl;

	@ApiModelProperty("标题")
	private String title;

	@ApiModelProperty("点赞数")
	private Integer likeCount;

	@ApiModelProperty("评论")
	private String comment;

	@ApiModelProperty("主题图")
	private String mainImage;

	@ApiModelProperty("是否点赞（0 否 1 是）")
	private Integer isLike;

	@ApiModelProperty("状态 1 未审核 2 审核通过 3 审核失败")
	private Integer status;

}
