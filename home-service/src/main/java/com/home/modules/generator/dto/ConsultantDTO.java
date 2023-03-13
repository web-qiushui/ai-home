package com.home.modules.generator.dto;

import com.home.common.utils.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;

/**
 * 顾问表
 * 
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-11 21:12:12
 */
@Data
@Builder
@ApiModel
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ConsultantDTO extends PageReq implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("Id")
	protected Integer id;

	@ApiModelProperty("类型(1 客户顾问 2 置业顾问)")
	private Integer type;

	@ApiModelProperty(value="状态（1 认证中 2 认证成功 3 认证失败）")
	private Integer status;

	@ApiModelProperty("手机号")
	private String phone;

	@ApiModelProperty("身份证号")
	private String idCard;

	@ApiModelProperty("姓名")
	private String name;

	@ApiModelProperty(value="用户Id", hidden = true)
	private Integer userId;

	@ApiModelProperty(value="企业Id", hidden = true)
	private Integer firmId;

	@ApiModelProperty("所属项目")
	private String project;

}
