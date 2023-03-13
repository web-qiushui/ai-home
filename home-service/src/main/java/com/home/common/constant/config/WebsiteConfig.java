package com.home.common.constant.config;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 网站配置
 */
@Data
@ApiModel
public class WebsiteConfig {

    @ApiModelProperty("网站名称")
    @NotNull(message = "网站名称不能为空")
    private String appName;

    @ApiModelProperty("网站Logo")
    private String appLogo;

    @ApiModelProperty("网站主Logo")
    private String appMainLogo;

    @ApiModelProperty("启动页图片")
    private String startPage;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("版权信息")
    private String copyright;

    @ApiModelProperty("备案号")
    private String record;

    @ApiModelProperty("客服电话")
    private String consumerPhone;

    @ApiModelProperty("客服链接")
    private String consumerLink;

    @ApiModelProperty("办公时间")
    private String officeHours;

    @ApiModelProperty("公众号二维码")
    private String thePublicQrCode;
}
