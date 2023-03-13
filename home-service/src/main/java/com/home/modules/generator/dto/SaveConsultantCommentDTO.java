package com.home.modules.generator.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 用户顾问
 * 
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-11 21:12:12
 */
@Data
@ApiModel
public class SaveConsultantCommentDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "用户Id", hidden = true)
	private Integer userId;

	@ApiModelProperty(value = "顾问Id", required = true)
	@NotNull(message = "顾问Id不能为空")
	private Integer consultentId;

	@ApiModelProperty("评论")
	private String comment;

	@ApiModelProperty(value = "打星（1-5）", required = true)
	@NotNull(message = "评价不能为空")
	private Integer star;

	@ApiModelProperty(value = "随机UUID")
	private String uuid;

}
