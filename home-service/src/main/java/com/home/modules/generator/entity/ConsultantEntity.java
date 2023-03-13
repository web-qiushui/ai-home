package com.home.modules.generator.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.home.common.utils.BeanEntity;
import lombok.*;

import java.io.Serializable;

/**
 * 顾问表
 * 
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-11 21:12:12
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("home_consultant")
public class ConsultantEntity extends BeanEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 类型（1 客户顾问 2 置业顾问）
	 */
	private Integer type;
	/**
	 * 状态（1 认证中 2 认证成功 3 认证失败）
	 */
	private Integer status;
	/**
	 * 星级（1-5）
	 */
	private Integer star;
	/**
	 * 咨询次数
	 */
	private Integer advisoryCount;
	/**
	 * 推荐置业次数
	 */
	private Integer buyHomeCount;
	/**
	 * 手机号
	 */
	private String phone;
	/**
	 * 身份证号
	 */
	private String idCard;
	/**
	 * 姓名
	 */
	private String name;
	/**
	 * 头像
	 */
	private String avatar;
	/**
	 * 用户Id
	 */
	private Integer homeUserId;
	/**
	 * 企业Id
	 */
	private Integer firmId;
	/**
	 * 项目信息
	 */
	private String project;

}
