package com.home.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.home.common.utils.PageBean;
import com.home.common.utils.PageReq;
import com.home.common.utils.PageUtils;
import com.home.modules.generator.dto.CarouselDTO;
import com.home.modules.generator.entity.CarouselEntity;
import com.home.modules.generator.vo.CarouselVO;

import java.util.List;
import java.util.Map;

/**
 * 轮播图表
 *
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-22 20:41:42
 */
public interface CarouselService extends IService<CarouselEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 查询轮播图
     */
    List<CarouselVO> getCarouselList();

    /**
     * 查询轮播图列表
     */
    PageBean<CarouselVO> getCarouselList(CarouselDTO dto);

}

