package com.home.common.constant.config;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 开关
 */
@Data
@ApiModel
public class SwitchConfig {

    @ApiModelProperty("键名")
    @NotNull(message = "键名不能为空")
    private String key;

    @ApiModelProperty("键值（true 启用 false 禁用）")
    @NotNull(message = "状态不能为空")
    private Boolean value;

    @ApiModelProperty("名称")
    @NotNull(message = "名称不能为空")
    private String name;
}
