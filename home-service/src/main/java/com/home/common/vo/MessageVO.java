package com.home.common.vo;


import com.home.common.utils.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 消息发送实体
 * @author xiewei
 */
@Data
@ApiModel
public class MessageVO implements Serializable {
    @ApiModelProperty("Id")
    private Integer id;

    @ApiModelProperty(value = "创建时间", example = "yyyy-MM-dd HH:mm:dd")
    private Date createTime;

    @ApiModelProperty("发送人（用户Id）")
    private Integer sendId;

    @ApiModelProperty("用户顾问表Id")
    private Integer consultantUserId;

    @ApiModelProperty("接收人（用户Id）")
    private Integer receiveId;

    @ApiModelProperty("内容")
    private String content;

    @ApiModelProperty("消息类型（1、文字 2、测评 3、购房报告 4、评价 5、置业顾问名片 6、照片）")
    private Integer type;

    @ApiModelProperty("读取状态（1 未读 2 已读）")
    private Integer status;
}
