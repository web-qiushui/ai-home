package com.home.modules.generator.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.home.common.utils.BeanEntity;
import lombok.*;

/**
 * 企业表
 * 
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-28 22:14:45
 */
@Data
@TableName("home_firm")
@EqualsAndHashCode(callSuper = true)
public class FirmEntity extends BeanEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 企业名称
	 */
	private String name;
	/**
	 * 企业地址
	 */
	private String address;
	/**
	 * 营业执照
	 */
	private String license;

}
