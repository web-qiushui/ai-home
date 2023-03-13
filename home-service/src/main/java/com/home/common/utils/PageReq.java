package com.home.common.utils;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@ApiModel
public class PageReq {

    private static final Integer DEFAULT_PAGE = 1;
    private static final Integer DEFAULT_SIZE = 10;


    @ApiModelProperty(value = "页码，不传默认为1")
    private Integer page = DEFAULT_PAGE;

    @ApiModelProperty(value = "每页个数：不传默认为10")
    private Integer size = DEFAULT_SIZE;

    @ApiModelProperty(value = "开始时间（yyyy-MM-dd）", example = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startTime;

    @ApiModelProperty(value = "结束时间（yyyy-MM-dd）", example = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    @ApiModelProperty(value = "降序字段")
    private List<String> desc;

    @ApiModelProperty(value = "升序字段")
    private List<String> asc;

    @ApiModelProperty(value = "排序对象", hidden = true)
    private List<OrderBy> orderBy;

    @Data
    private static class OrderBy {

        @ApiModelProperty(value = "列明", hidden = true)
        private String column;

        @ApiModelProperty(value = "排序类型：ASC升序，DESC降序", hidden = true)
        private String  type;
    }

    /**
     * 设置排序
     * @param desc
     * @param asc
     */
    public void setOrderBy(List<String> desc, List<String> asc) {
        List<OrderBy> list = new ArrayList<>();
        if (desc != null && desc.size() > 0) {
            desc.forEach(o -> {
                OrderBy orderBy = new OrderBy();
                orderBy.setColumn(o);
                orderBy.setType("DESC");
                list.add(orderBy);
            });
        }

        if (asc != null && asc.size() > 0) {
            asc.forEach(o -> {
                OrderBy orderBy = new OrderBy();
                orderBy.setColumn(o);
                orderBy.setType("ASC");
                list.add(orderBy);
            });
        }

        if (list.size() == 0) {
            this.orderBy = null;
        } else {
            this.orderBy = list;
        }
    }

    public void setPage(Integer page) {
        if (page == null) {
            this.page = DEFAULT_PAGE;
        } else {
            this.page = page;
        }
    }

    public void setSize(Integer size) {
        if (size == null) {
            this.size = DEFAULT_SIZE;
        } else if (size < 1) {
            this.size = 1;
        } else {
            this.size = size;
        }
    }

}
