package com.home.modules.generator.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 轮播图表
 * 
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-22 20:41:42
 */
@Data
public class LoginStatisticslVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("日数据")
	private Integer dayData;

	@ApiModelProperty("月数据")
	private Integer monthData;

	@ApiModelProperty("总数据")
	private Integer totalData;

}
