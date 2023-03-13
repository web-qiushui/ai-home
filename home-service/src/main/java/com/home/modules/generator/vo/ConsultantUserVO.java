package com.home.modules.generator.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户顾问
 * 
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-18 00:06:25
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConsultantUserVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("id")
	private Integer id;

	@ApiModelProperty("创建时间")
	private Date createTime;

	@ApiModelProperty("顾问表Id")
	private Integer consultantId;

	@ApiModelProperty("用户Id")
	private Integer userId;

	@ApiModelProperty("顾问用户Id")
	private Integer consultantUserId;

	@ApiModelProperty("用户昵称")
	private String nickName;

	@ApiModelProperty("顾问姓名")
	private String name;

	@ApiModelProperty("用户头像")
	private String avatarUrl;

	@ApiModelProperty("顾问头像")
	private String avatar;

	@ApiModelProperty("消息")
	private String news;

	@ApiModelProperty("消息类型（1、文字 2、测评 3、购房报告 4、评价 5、置业顾问名片 6、照片 7、购房需求）")
	private Integer newsType;

	@ApiModelProperty("类型（1 客户顾问 2 置业顾问）")
	private Integer type;

	@ApiModelProperty("未读消息数")
	private Integer unread;

}
