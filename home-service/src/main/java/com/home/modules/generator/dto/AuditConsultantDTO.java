package com.home.modules.generator.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 顾问表
 * 
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-11 21:12:12
 */
@Data
@ApiModel
public class AuditConsultantDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("Id")
	protected Integer id;

	@ApiModelProperty("类型(1 客户顾问 2 置业顾问)")
	private Integer type;

	@ApiModelProperty(value="状态（1 审核中 2 审核成功 3 审核失败）")
	private Integer status;

	@ApiModelProperty(value = "备注")
	private String remark;

	@ApiModelProperty(value="操作人Id", hidden = true)
	private Long operateId;

	@ApiModelProperty(value="企业Id")
	private Long firmId;


}
