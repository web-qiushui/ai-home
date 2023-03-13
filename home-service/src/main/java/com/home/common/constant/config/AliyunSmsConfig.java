package com.home.common.constant.config;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 阿里云短信配置
 */
@Data
@ApiModel
public class AliyunSmsConfig {

    @ApiModelProperty("阿里云accessKey")
    @NotNull(message = "阿里云accessKey不能为空")
    private String accessKey;

    @ApiModelProperty("阿里云accessSecret")
    @NotNull(message = "阿里云accessSecret不能为空")
    private String accessSecret;

    @ApiModelProperty("阿里云API地址")
    @NotNull(message = "阿里云API地址不能为空")
    private String url;

    @ApiModelProperty("短信签名")
    @NotNull(message = "短信签名不能为空")
    private String sign;

    @ApiModelProperty("模版Id")
    @NotNull(message = "模版Id不能为空")
    private String templateCode;

    @ApiModelProperty("状态（0 禁用 1 启用）")
    @NotNull(message = "状态不能为空")
    private Integer status;
}
