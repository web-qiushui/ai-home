package com.home.modules.generator.dto;

import com.home.common.utils.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
@EqualsAndHashCode(callSuper = true)
public class ConsultantCommentDTO extends PageReq implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "用户Id", hidden = true)
	private Integer userId;

	@ApiModelProperty(value = "顾问Id", required = true)
	private Integer consultantId;

}
