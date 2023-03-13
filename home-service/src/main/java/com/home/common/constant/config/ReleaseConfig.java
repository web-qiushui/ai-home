package com.home.common.constant.config;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 注意事项配置
 */
@Data
@ApiModel
public class ReleaseConfig {

    @ApiModelProperty("中间描述（人数用$count$表示）")
    @NotNull(message = "中间描述不能为空")
    private String inDescription;

    @ApiModelProperty("顶部描述")
    @NotNull(message = "顶部描述不能为空")
    private String topDescription;

    @ApiModelProperty("基准人数")
    @NotNull(message = "基准人数不能为空")
    private Integer baseCount;

}
