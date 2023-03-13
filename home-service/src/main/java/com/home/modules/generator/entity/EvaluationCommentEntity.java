package com.home.modules.generator.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.home.common.utils.BeanEntity;
import lombok.*;

import java.io.Serializable;

/**
 * 测评评论表
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
@TableName("home_evaluation_comment")
public class EvaluationCommentEntity extends BeanEntity implements Serializable {
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
	 * 评论
	 */
	private String comment;
	/**
	 * 点赞数
	 */
	private Integer likeCount;
	/**
	 * 状态 1 未审核 2 审核通过 3 审核失败
	 */
	private Integer status;
	/**
	 * 操作人Id
	 */
	private Integer operateId;
	/**
	 * 备注
	 */
	private String remark;

}
