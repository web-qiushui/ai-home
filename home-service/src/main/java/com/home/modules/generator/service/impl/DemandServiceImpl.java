package com.home.modules.generator.service.impl;

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.home.common.utils.*;
import com.home.common.vo.DatadictVO;
import com.home.modules.generator.dto.DemandDTO;
import com.home.modules.generator.vo.DemandVO;
import com.home.modules.sys.service.SysDatadictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.home.modules.generator.dao.DemandDao;
import com.home.modules.generator.entity.DemandEntity;
import com.home.modules.generator.service.DemandService;

import javax.annotation.Resource;

@Slf4j
@Service("demandService")
public class DemandServiceImpl extends ServiceImpl<DemandDao, DemandEntity> implements DemandService {

    @Resource
    private SysDatadictService sysDatadictService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<DemandEntity> page = this.page(
                new Query<DemandEntity>().getPage(params),
                new QueryWrapper<DemandEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageBean<DemandVO> getMyPublishByUserId(PageReq page, Integer userId) {
        PageHelper.startPage(page.getPage(), page.getSize());
        List<DemandVO> demandList = baseMapper.getMyPublishByUserId(userId);
        PageBean<DemandVO> pageBean = new PageBean<>(demandList);
        demandList.forEach(vo -> {
            vo.setLabel(JSON.parseArray(vo.getLabelStr(), String.class));
        });
        pageBean.setList(demandList);
        return pageBean;
    }

    @Override
    public PageBean<DemandVO> getDemandList(DemandDTO dto) {
        PageHelper.startPage(dto.getPage(), dto.getSize());
        List<DemandVO> demandList = baseMapper.getDemandList(dto);
        PageBean<DemandVO> pageBean = new PageBean<>(demandList);
        demandList.forEach(vo -> {
            vo.setLabel(JSON.parseArray(vo.getLabelStr(), String.class));
        });
        pageBean.setList(demandList);
        return pageBean;
    }

    @Override
    public Integer getDemandSize() {
        int size =  0;
        try {
            Integer count = baseMapper.selectCount(new QueryWrapper<>());
            DatadictVO datadict = sysDatadictService.getDatadict(Constant.Datadict.DEMAND_SIZE);
            Integer demandSize = Convert.toInt(datadict.getValue());
            size = count + demandSize;
        } catch (Exception e) {
            log.error("查询购房需求数量失败", e);
        }
        return size;
    }
}