package com.home.modules.generator.dto;

import com.home.common.utils.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;

/**
 * 测评表
 * 
 * @author yaokai
 * @email 906147469@qq.com
 * @date 2020-06-11 21:12:13
 */
@Data
@Builder
@ApiModel
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DemandDTO extends PageReq implements Serializable {

	@ApiModelProperty("主键TableId")
	private Integer id;

	@ApiModelProperty("区域")
	private String region;

	@ApiModelProperty("价格")
	private String price;

	@ApiModelProperty("面积")
	private String area;

	@ApiModelProperty("标签")
	private List<String> label;

	@NotNull(message = "说明不能为空")
	@ApiModelProperty("说明")
	private String explanation;

	@ApiModelProperty("电话")
	private String phone;

	@ApiModelProperty("意向楼盘")
	private String intentions;

	@ApiModelProperty(value = "用户Id", hidden = true)
	private Integer userId;

	@ApiModelProperty(value = "ai顾问用户Id", hidden = true)
	private Integer aiConsultantId;

}
