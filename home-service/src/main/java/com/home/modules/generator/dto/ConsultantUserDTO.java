package com.home.modules.generator.dto;

import com.home.common.utils.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * 用户顾问
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
public class ConsultantUserDTO extends PageReq implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("类型(1 客户顾问 2 置业顾问)")
	private Integer type;

	@ApiModelProperty(value="用户Id", hidden = true)
	private Integer userId;

	@ApiModelProperty(value="状态", hidden = true)
	private Integer status;

	@ApiModelProperty(value="企业Id", hidden = true)
	private List<Integer> firmIds;

}
