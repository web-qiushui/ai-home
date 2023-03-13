package com.home.modules.generator.dto;

import com.home.common.utils.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
public class EvaluationForwardDTO extends PageReq {

    @ApiModelProperty("搜索")
    private String search;
}
