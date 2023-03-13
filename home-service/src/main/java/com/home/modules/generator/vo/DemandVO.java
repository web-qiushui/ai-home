package com.home.modules.generator.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 购房需求
 * 
 * @author yaokai
 * @email 906147469@qq.com
 * @date 2020-06-11 21:12:13
 */
@Data
@ApiModel
public class DemandVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("主键TableId")
	private Integer id;

	@ApiModelProperty(value = "创建时间", example = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	@ApiModelProperty("区域")
	private String region;

	@ApiModelProperty("价格")
	private String price;

	@ApiModelProperty("面积")
	private String area;

	@ApiModelProperty("标签")
	private List<String> label;

	@ApiModelProperty("标签字符串")
	private String labelStr;

	@ApiModelProperty("说明")
	private String explanation;

	@ApiModelProperty("电话")
	private String phone;

	@ApiModelProperty("意向楼盘")
	private String intentions;

	@ApiModelProperty("昵称")
	private String nickName;

	@ApiModelProperty("头像")
	private String avatarUrl;

}
