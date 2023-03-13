package com.home.modules.generator.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.home.common.utils.BeanEntity;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 顾问审核记录表
 * 
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-21 23:41:37
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("home_consultant_audit")
public class ConsultantAuditEntity extends BeanEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 顾问Id
	 */
	private Integer homeConsultantId;
	/**
	 * 状态（0 拒绝 1 同意 ）
	 */
	private Integer status;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 类型（1、申请 2、平台审核 3、企业审核）
	 */
	private Integer type;
	/**
	 * 操作人Id
	 */
	private Integer operateId;
	/**
	 * 操作人Id
	 */
	private Date operateTime;

}
