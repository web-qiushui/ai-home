package com.home.modules.generator.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.home.common.utils.BeanEntity;
import lombok.*;

import java.io.Serializable;

/**
 * 测评转发表
 * 
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-18 23:57:55
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("home_evaluation_forward")
public class EvaluationForwardEntity extends BeanEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 测评表Id
	 */
	private Integer homeEvaluationId;
	/**
	 * 用户Id
	 */
	private Integer homeUserId;
	/**
	 * 转发次数
	 */
	private Integer forwardCount;


}
