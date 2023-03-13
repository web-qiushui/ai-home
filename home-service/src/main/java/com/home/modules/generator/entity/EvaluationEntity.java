package com.home.modules.generator.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.home.common.utils.BeanEntity;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 测评表
 * 
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-11 21:12:13
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("home_evaluation")
public class EvaluationEntity extends BeanEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 状态 0 隐藏 1 显示
	 */
	private Integer status;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 作者
	 */
	private String author;
	/**
	 * 原创
	 */
	private String original;
	/**
	 * 公众号名称
	 */
	private String thePublicName;
	/**
	 * 描述
	 */
	private String description;
	/**
	 * 点赞数
	 */
	private Integer likeCount;
	/**
	 * 评论数
	 */
	private Integer commentCount;
	/**
	 * 收藏数
	 */
	private Integer collectCount;
	/**
	 * 转发数
	 */
	private Integer forwardCount;
	/**
	 * 标签
	 */
	private String label;
	/**
	 * 类型 1 测评报告 2 快讯
	 */
	private Integer type;
	/**
	 * 主题图
	 */
	private String mainImage;

	/**
	 * 区域
	 */
	private String region;
	/**
	 * 价格
	 */
	private String price;
	/**
	 * 面积
	 */
	private String area;
	/**
	 * 用户Id
	 */
	private Integer homeUserId;
	/**
	 * 管理员Id
	 */
	private Integer sysUserId;


}
