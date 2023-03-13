package com.home.common.constant.config;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 聊天卡片配置
 */
@Data
@ApiModel
public class ChatCardConfig {

    @ApiModelProperty("名称")
    @NotNull(message = "名称不能为空")
    private String name;

    @ApiModelProperty("图片路径")
    @NotNull(message = "图片路径不能为空")
    private String imagesUrl;

    @ApiModelProperty("编码")
    @NotNull(message = "编码不能为空")
    private String code;

    @ApiModelProperty("权限（0 全部 1 用户 2 AI顾问 3 置业顾问 4 顾问）")
    @NotNull(message = "编码不能为空")
    private Integer auth;

    @ApiModelProperty("排序")
    @NotNull(message = "排序不能为空")
    private Integer sort;

    @ApiModelProperty("状态（0 禁用 1 启用）")
    @NotNull(message = "状态不能为空")
    private Integer status;

}
