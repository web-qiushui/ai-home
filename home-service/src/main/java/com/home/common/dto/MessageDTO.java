package com.home.common.dto;

import com.home.common.utils.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 消息接收对象
 * @author xiewei
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
public class MessageDTO extends PageReq implements Serializable {

    @ApiModelProperty("发送人（用户Id）")
    private Integer sendId;

    @ApiModelProperty("用户顾问表Id")
    private Integer consultantUserId;

    @ApiModelProperty("接收人（用户Id）")
    private Integer receiveId;

    @ApiModelProperty("内容")
    private String content;

    @ApiModelProperty("消息类型（1、文字 2、测评 3、购房报告 4、评价 5、置业顾问名片 6、照片 7 快速回复）")
    private Integer type;

}
