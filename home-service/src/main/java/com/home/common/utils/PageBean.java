package com.home.common.utils;

import com.github.pagehelper.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collection;
import java.util.List;

@Data
@ApiModel
public class PageBean<T> {

    @ApiModelProperty("当前页")
    private Integer pageNum = 1;

    @ApiModelProperty("每页显示的总条数")
    private Integer pageSize = 10;

    @ApiModelProperty("当前页的条数")
    private Integer size;

    @ApiModelProperty("总条数")
    private Long total;

    @ApiModelProperty("总页数")
    private Integer pages;

    @ApiModelProperty("结果集")
    private List<T> list;

    public PageBean(){

    }

    public PageBean(List<T> list) {
        if (list instanceof Page) {
            Page page = (Page)list;
            this.pageNum = page.getPageNum();
            this.pageSize = page.getPageSize();
            this.pages = page.getPages();
            this.list = page;
            this.size = page.size();
            this.total = page.getTotal();
        } else if (list != null) {
            this.pageNum = 1;
            this.pageSize = list.size();
            this.pages = this.pageSize > 0 ? 1 : 0;
            this.list = list;
            this.size = list.size();
            this.total = (long)list.size();
        }
    }

}
