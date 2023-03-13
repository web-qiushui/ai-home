package com.home.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.home.common.utils.BeanEntity;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * 系统配置信息
 *
 * @author xw
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_config")
public class SysConfigEntity {

	/**
	 * 主键
	 */
	@TableId(value = "id", type = IdType.AUTO)
	protected Integer id;

	/**
	 * 创建时间
	 */
	@TableField(value = "create_time",fill = FieldFill.INSERT)
	protected Date createTime;

	/**
	 * 修改时间
	 */
	@TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
	protected Date updateTime;

	/**
	 * 是否删除 0 是 1 否
	 */
	@TableField(value = "is_delete",fill = FieldFill.INSERT)
	private Integer isDelete;
	/**
	 * 创建人
	 */
	private Integer createBy;

	/**
	 * key
	 */
	@NotBlank(message="参数名不能为空")
	private String paramKey;

	/**
	 * value
	 */
	@NotBlank(message="参数值不能为空")
	private String paramValue;

	/**
	 * 备注
	 */
	private String remark;

}
