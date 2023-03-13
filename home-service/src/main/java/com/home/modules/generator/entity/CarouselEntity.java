package com.home.modules.generator.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.home.common.utils.BeanEntity;
import lombok.*;

import java.io.Serializable;

/**
 * 轮播图表
 * 
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-22 20:41:42
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("home_carousel")
public class CarouselEntity extends BeanEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 轮播图
	 */
	private String imageUrl;
	/**
	 * 描述
	 */
	private String description;
	/**
	 * 状态（0 禁用 1 启用）
	 */
	private Integer status;
	/**
	 * 排序
	 */
	private Integer sort;
	/**
	 * 页面路径
	 */
	private String pagePath;

}
