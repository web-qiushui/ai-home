package com.home.modules.generator.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 测评详情表
 * 
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-07-15 19:48:57
 */
@Data
@TableName("home_evaluation_details")
public class EvaluationDetailsEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 测评表Id
	 */
	@TableId(type = IdType.INPUT)
	private Integer id;
	/**
	 * 内容
	 */
	private String content;

}
