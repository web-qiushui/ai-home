package com.home.modules.generator.dto;

import com.home.common.utils.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 测评
 * 
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-11 21:12:12
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
public class EvaluationCommentDTO extends PageReq implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "用户Id", hidden = true)
	private Integer userId;

	@ApiModelProperty(value = "顾问Id", required = true)
	private Integer evaluationId;

	@ApiModelProperty(value = "状态 1 未审核 2 审核通过 3 审核失败")
	private Integer status;

	@ApiModelProperty(value = "备注")
	private String remark;

}
