package com.home.common.dto;

import com.home.common.utils.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;


/**
 * 微信登录
 *
 * @author xiewei
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
public class UserDTO extends PageReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户Id")
    private Integer id;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("身份证")
    private String idCard;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("性别（0：未知，1：男，2：女）")
    private Integer gender;

    @ApiModelProperty("头像")
    private String avatarUrl;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("角色（0 用户 1 AI顾问 2 置业顾问）")
    private Integer role;

}
