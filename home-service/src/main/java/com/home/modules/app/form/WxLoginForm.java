package com.home.modules.app.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 微信登录表单
 *
 * @author xw
 */
@Data
@ApiModel(value = "微信登录表单")
public class WxLoginForm {

    @ApiModelProperty(value = "code")
    @NotBlank(message="code不能为空")
    private String code;

}
