package com.home.common.constant.config;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 金刚区配置
 */
@Data
@ApiModel
public class KingKongConfig {

    @ApiModelProperty("名称")
    @NotNull(message = "名称不能为空")
    private String name;

    @ApiModelProperty("图片路径")
    @NotNull(message = "图片路径不能为空")
    private String imagesUrl;

    @ApiModelProperty("类型")
    @NotNull(message = "类型不能为空")
    private String type;

    @ApiModelProperty("状态（0 禁用 1 启用）")
    @NotNull(message = "状态不能为空")
    private Integer status;

    @ApiModelProperty("测评Id列表")
    private List<Integer> evaluationIds;
}
