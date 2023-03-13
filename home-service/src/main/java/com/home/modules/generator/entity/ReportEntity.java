package com.home.modules.generator.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.home.common.utils.BeanEntity;
import lombok.*;

/**
 * 购房报告表
 * 
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-07-25 13:53:42
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("home_report")
public class ReportEntity extends BeanEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 楼盘
	 */
	private String realEstate;
	/**
	 * 地址
	 */
	private String address;
	/**
	 * 品牌
	 */
	private String brand;
	/**
	 * 价格
	 */
	private String price;
	/**
	 * 项目优势
	 */
	private String projectAdvantage;
	/**
	 * 项目劣势
	 */
	private String projectDisadvantage;
	/**
	 * 匹配度
	 */
	private String suitability;
	/**
	 * 相关推荐
	 */
	private String relatedSuggestion;
	/**
	 * 优惠
	 */
	private String discount;
	/**
	 * 版权
	 */
	private String copyright;
	/**
	 * 免责声明
	 */
	private String disclaimer;
	/**
	 * 用户Id
	 */
	private Integer homeUserId;
	/**
	 * 顾问Id
	 */
	private Integer homeConsultantId;
	/**
	 * 购房报告Id
	 */
	private Integer homeDemandId;

	/**
	 * 购房报告图片地址
	 */
	private String imageUrl;

}
