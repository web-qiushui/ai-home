package com.home.common.constant.config;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 微信小程序配置
 */
@Data
@ApiModel
public class WxConfig {

    @ApiModelProperty("小程序appId")
    @NotNull(message = "小程序appId不能为空")
    private String appId;

    @ApiModelProperty("小程序appSecret")
    @NotNull(message = "小程序appSecret不能为空")
    private String appSecret;

}
