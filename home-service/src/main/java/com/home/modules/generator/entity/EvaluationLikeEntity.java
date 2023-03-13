package com.home.modules.generator.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.home.common.utils.BeanEntity;
import lombok.*;

import java.io.Serializable;

/**
 * 测评点赞表
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
@TableName("home_evaluation_like")
public class EvaluationLikeEntity extends BeanEntity implements Serializable {
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
	 * 状态（0 失效 1 生效）
	 */
	private Integer status;

}
