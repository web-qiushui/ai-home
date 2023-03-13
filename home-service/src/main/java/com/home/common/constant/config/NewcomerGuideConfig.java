package com.home.common.constant.config;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 新人引导
 */
@Data
@ApiModel
public class NewcomerGuideConfig {

    @ApiModelProperty("说明")
    private String explanation;

    @ApiModelProperty("类型")
    @NotNull(message = "类型不能为空")
    private String type;

    @ApiModelProperty("状态（0 禁用 1 启用）")
    @NotNull(message = "状态不能为空")
    private Integer status;
}
