package com.home.common.constant.config;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 快速回复配置
 */
@Data
@ApiModel
public class QuickReplyConfig {

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("回复内容")
    @NotNull(message = "回复内容不能为空")
    private String content;

    @ApiModelProperty("权限（0 全部、1 用户、2 AI顾问、3 置业顾问）")
    @NotNull(message = "权限不能为空")
    private Integer type;

    @ApiModelProperty("状态（0 禁用 1 启用）")
    @NotNull(message = "状态不能为空")
    private Integer status;
}
