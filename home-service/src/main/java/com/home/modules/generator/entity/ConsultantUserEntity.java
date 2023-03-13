package com.home.modules.generator.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.home.common.utils.BeanEntity;
import lombok.*;

import java.io.Serializable;

/**
 * 用户顾问表
 * 
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-18 00:06:25
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("home_consultant_user")
public class ConsultantUserEntity extends BeanEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 顾问表Id
	 */
	private Integer homeConsultantId;
	/**
	 * 顾问表Id（AI推荐）
	 */
	private Integer recommendId;
	/**
	 * 用户Id
	 */
	private Integer homeUserId;
	/**
	 * 顾问用户Id
	 */
	private Integer homeConsultantUserId;
	/**
	 * 状态（0 拉黑 1 正常）
	 */
	private Integer status;

}
