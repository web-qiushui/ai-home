package com.home.modules.generator.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.home.common.utils.BeanEntity;
import lombok.*;

import java.io.Serializable;

/**
 * 顾问评论表
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
@TableName("home_consultant_comment")
public class ConsultantCommentEntity extends BeanEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 顾问Id
	 */
	private Integer homeConsultantId;
	/**
	 * 评论
	 */
	private String comment;
	/**
	 * 打星（1-5）
	 */
	private Integer star;
	/**
	 * 用户Id
	 */
	private Integer homeUserId;

}
