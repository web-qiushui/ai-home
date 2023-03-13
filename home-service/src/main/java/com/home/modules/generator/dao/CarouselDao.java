package com.home.modules.generator.dao;

import com.home.modules.generator.dto.CarouselDTO;
import com.home.modules.generator.entity.CarouselEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.home.modules.generator.vo.CarouselVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 轮播图表
 * 
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-22 20:41:42
 */
@Mapper
public interface CarouselDao extends BaseMapper<CarouselEntity> {

    /**
     * 查看轮播图列表
     * @return
     */
    List<CarouselVO> getCarouselList(CarouselDTO dto);
	
}
