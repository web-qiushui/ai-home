package com.home.modules.generator.service.impl;

import com.github.pagehelper.PageHelper;
import com.home.common.utils.*;
import com.home.modules.generator.dto.CarouselDTO;
import com.home.modules.generator.vo.CarouselVO;
import com.home.modules.generator.vo.EvaluationCommentVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.home.modules.generator.dao.CarouselDao;
import com.home.modules.generator.entity.CarouselEntity;
import com.home.modules.generator.service.CarouselService;

@Slf4j
@Service("carouselService")
public class CarouselServiceImpl extends ServiceImpl<CarouselDao, CarouselEntity> implements CarouselService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CarouselEntity> page = this.page(
                new Query<CarouselEntity>().getPage(params),
                new QueryWrapper<CarouselEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CarouselVO> getCarouselList() {
        QueryWrapper<CarouselEntity> queryWrapper = new QueryWrapper<CarouselEntity>().eq("status", Constant.Status.ENABLE.getType());
        List<CarouselEntity> carouselEntities = baseMapper.selectList(queryWrapper);
        List<CarouselVO> list = new ArrayList<>();
        carouselEntities.forEach(carouselEntity -> {
            CarouselVO carouselVO = new CarouselVO();
            BeanUtils.copyProperties(carouselEntity, carouselVO);
            list.add(carouselVO);
        });
        return list;
    }

    @Override
    public PageBean<CarouselVO> getCarouselList(CarouselDTO dto) {
        PageHelper.startPage(dto.getPage(), dto.getSize());
        List<CarouselVO> carouselList = baseMapper.getCarouselList(dto);
        PageBean<CarouselVO> pageBean = new PageBean<>(carouselList);
        return pageBean;
    }

}