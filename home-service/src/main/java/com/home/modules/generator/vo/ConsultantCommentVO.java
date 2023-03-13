package com.home.modules.generator.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 顾问评论表
 * 
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-11 21:12:12
 */
@Data
public class ConsultantCommentVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("id")
	protected Integer id;

	@ApiModelProperty(value = "创建时间", example = "yyyy-MM-dd HH:mm:ss")
	protected Date createTime;

	@ApiModelProperty("顾问Id")
	private Integer consultantId;

	@ApiModelProperty("评论")
	private String comment;

	@ApiModelProperty("打星（1-5）")
	private Integer star;

	@ApiModelProperty("用户Id")
	private Integer userId;

	@ApiModelProperty("姓名")
	private String name;

	@ApiModelProperty("头像")
	private String avatar;

}
