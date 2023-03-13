package com.home.modules.generator.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.home.common.utils.BeanEntity;
import lombok.*;

import java.io.Serializable;

/**
 * 开盘预告表
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
@TableName("home_opening_notice")
public class OpeningNoticeEntity extends BeanEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 描述
	 */
	private String description;
	/**
	 * 图片
	 */
	private String imageUrl;
	/**
	 * 状态（0 禁用 1 启用）
	 */
	private Integer status;

}
