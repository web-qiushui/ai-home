package com.home.modules.generator.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.home.common.utils.BeanEntity;
import lombok.*;

import java.io.Serializable;

/**
 * 购房需求表
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
@TableName("home_demand")
public class DemandEntity extends BeanEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 区域
	 */
	private String region;
	/**
	 * 价格
	 */
	private String price;
	/**
	 * 面积
	 */
	private String area;
	/**
	 * 标签
	 */
	private String label;
	/**
	 * 说明
	 */
	private String explanation;
	/**
	 * 电话
	 */
	private String phone;
    /**
     * AI顾问Id
     */
    private Integer aiConsultantId;
    /**
     * 置业顾问Id
     */
    private Integer homeConsultantId;
    /**
     * 意向楼盘
     */
    private String intentions;
    /**
     * 用户Id
     */
    private Integer homeUserId;
    /**
     * 是否删除 0 是 1 否
     */
    @TableField(value = "is_delete",fill = FieldFill.INSERT)
    private Integer isDelete;

}
