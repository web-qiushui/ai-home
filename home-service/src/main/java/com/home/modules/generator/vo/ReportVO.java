package com.home.modules.generator.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 购房报告表
 * 
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-07-25 13:53:42
 */
@Data
@ApiModel
public class ReportVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("主键")
	protected Integer id;

	@ApiModelProperty("创建时间")
	private Date createTime;

	@ApiModelProperty("楼盘")
	private String realEstate;

	@ApiModelProperty("地址")
	private String address;

	@ApiModelProperty("品牌")
	private String brand;

	@ApiModelProperty("价格")
	private String price;

	@ApiModelProperty("项目优势")
	private String projectAdvantage;

	@ApiModelProperty("项目劣势")
	private String projectDisadvantage;

	@ApiModelProperty("匹配度")
	private String suitability;

	@ApiModelProperty("相关推荐")
	private String relatedSuggestion;

	@ApiModelProperty("优惠")
	private String discount;

	@ApiModelProperty("版权")
	private String copyright;

	@ApiModelProperty("免责声明")
	private String disclaimer;

	@ApiModelProperty("用户Id")
	private Integer homeUserId;

	@ApiModelProperty("顾问Id")
	private Integer homeConsultantId;

	@ApiModelProperty("购房报告Id")
	private Integer homeDemandId;

}
