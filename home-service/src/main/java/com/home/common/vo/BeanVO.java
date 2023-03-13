package com.home.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author xiewei
 */
@Data
@ApiModel
public class BeanVO {

    @ApiModelProperty("ID")
    private Integer id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("类型")
    private String type;

    @ApiModelProperty("关联ID")
    private Integer commonId;

}

