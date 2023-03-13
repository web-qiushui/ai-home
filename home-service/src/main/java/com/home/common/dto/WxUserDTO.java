package com.home.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


/**
 * 微信登录
 * @author xiewei
 */
@Data
@ApiModel
public class WxUserDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	@NotNull(message = "微信code不能为空")
	@ApiModelProperty(value = "微信code", required = true)
	private String code;

    @NotNull(message = "rawData不能为空")
    @ApiModelProperty(value = "不包括敏感信息的原始数据字符串，用于计算签名", required = true)
	private String rawData;

    @NotNull(message = "signature不能为空")
    @ApiModelProperty(value = "使用 sha1( rawData + sessionkey ) 得到字符串，用于校验用户信息", required = true)
	private String signature;

    @ApiModelProperty(value = "用户信息", hidden = true)
    private UserInfo userInfo;

    @Data
	public static class UserInfo {
        @ApiModelProperty("昵称")
        private String nickName;

        @ApiModelProperty("性别（0：未知，1：男，2：女）")
        private Integer gender;

        @ApiModelProperty("头像")
        private String avatarUrl;

        @ApiModelProperty("国家")
        private String country;

        @ApiModelProperty("省")
        private String province;

        @ApiModelProperty("市")
        private String city;
    }

}
