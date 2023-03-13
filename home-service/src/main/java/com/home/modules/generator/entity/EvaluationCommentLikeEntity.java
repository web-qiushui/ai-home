package com.home.modules.generator.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.home.common.utils.BeanEntity;
import lombok.*;

import java.io.Serializable;

/**
 * 测评评论点赞表
 * 
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-23 21:19:16
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("home_evaluation_comment_like")
public class EvaluationCommentLikeEntity extends BeanEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 测评评论表Id
	 */
	private Integer homeEvaluationCommentId;
	/**
	 * 用户Id
	 */
	private Integer homeUserId;
	/**
	 * 状态（0 失效 1 生效）
	 */
	private Integer status;

}
