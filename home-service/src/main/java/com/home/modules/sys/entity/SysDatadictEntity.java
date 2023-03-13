package com.home.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.home.common.utils.BeanEntity;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据字典
 * 
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2019-12-13 17:39:07
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_datadict")
public class SysDatadictEntity implements Serializable {
	private static final long serialVersionUID = 1L;

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
	 * 状态   0：禁用  1：启用
	 */
	private Integer status;

	/**
	 * 创建人
	 */
	private Integer createBy;

	/**
	 * 字典类型
	 */
	private String type;
	/**
	 * 字典名
	 */
	private String name;
	/**
	 * 字典值
	 */
	private String value;
	/**
	 * 扩展1
	 */
	private String n1;
	/**
	 * 扩展2
	 */
	private String n2;
	/**
	 * 扩展3
	 */
	private String n3;
	/**
	 * 扩展4
	 */
	private String n4;
	/**
	 * 扩展5（JSON）
	 */
	private String n5;

}
