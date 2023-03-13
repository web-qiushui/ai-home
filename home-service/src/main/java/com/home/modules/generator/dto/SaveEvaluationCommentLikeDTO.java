package com.home.modules.generator.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 测评评论点赞
 * 
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-11 21:12:12
 */
@Data
@ApiModel
public class SaveEvaluationCommentLikeDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	@NotNull(message = "测评评论Id不能为空")
	@ApiModelProperty(value = "测评评论表Id", required = true)
	private Integer evaluationCommentId;

	@ApiModelProperty(value = "用户Id", hidden = true)
	private Integer userId;

}
