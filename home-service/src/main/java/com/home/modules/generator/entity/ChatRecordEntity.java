package com.home.modules.generator.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.home.common.utils.BeanEntity;
import lombok.*;

import java.io.Serializable;

/**
 * 聊天记录表
 * 
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-25 10:00:21
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("home_chat_record")
public class ChatRecordEntity extends BeanEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 用户顾问表Id
	 */
	private Integer homeConsultantUserId;
	/**
	 * 发送人Id
	 */
	private Integer sendId;
	/**
	 * 接收人Id
	 */
	private Integer receiveId;
	/**
	 * 内容
	 */
	private String content;
	/**
	 * 消息类型（1、文字 2、测评 3、购房报告 4、评价 5、置业顾问名片 6、照片 7 快速回复）
	 */
	private Integer type;
	/**
	 * 读取状态（1 未读 2 已读）
	 */
	private Integer status;

}
