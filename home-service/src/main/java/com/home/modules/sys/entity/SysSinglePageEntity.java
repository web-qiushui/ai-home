package com.home.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import com.home.common.utils.BeanEntity;
import lombok.*;

/**
 * 系统单页面表
 * 
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-28 22:14:45
 */
@Data
@TableName("sys_single_page")
@EqualsAndHashCode(callSuper = true)
public class SysSinglePageEntity extends BeanEntity implements Serializable {
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
	 * 类型
	 */
	private Integer type;
	/**
	 * 编码
	 */
	private String code;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 内容
	 */
	private String content;
	/**
	 * 封面图
	 */
	private String coverUrl;
	/**
	 * 状态（0 禁用 1 启用）
	 */
	private Integer status;
	/**
	 * 是否删除 （0 是 1 否）
	 */
	@TableField(value = "is_delete",fill = FieldFill.INSERT)
	private Integer isDelete;

}
