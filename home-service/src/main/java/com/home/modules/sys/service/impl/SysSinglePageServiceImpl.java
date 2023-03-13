package com.home.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.home.common.dto.SinglePageDTO;
import com.home.common.exception.RRException;
import com.home.common.utils.JsonResult;
import com.home.common.utils.PageBean;
import com.home.common.vo.SinglePageVO;
import com.home.modules.sys.dao.SysSinglePageDao;
import com.home.modules.sys.entity.SysSinglePageEntity;
import com.home.modules.sys.service.SysSinglePageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.home.common.utils.PageUtils;
import com.home.common.utils.Query;


@Slf4j
@Service("sysSinglePageService")
public class SysSinglePageServiceImpl extends ServiceImpl<SysSinglePageDao, SysSinglePageEntity> implements SysSinglePageService {

    @Override
    public SinglePageVO getSinglePageByCode(String code) {
        LambdaQueryWrapper<SysSinglePageEntity> wrapper = Wrappers.<SysSinglePageEntity>lambdaQuery().eq(SysSinglePageEntity::getCode, code);
        SysSinglePageEntity sysSinglePageEntity = baseMapper.selectOne(wrapper);
        if (sysSinglePageEntity == null) {
            throw new RRException("编码错误");
        }
        SinglePageVO vo = new SinglePageVO();
        BeanUtils.copyProperties(sysSinglePageEntity, vo);
        return vo;
    }

    @Override
    public PageBean<SinglePageVO> getSinglePageList(SinglePageDTO dto) {
        PageHelper.startPage(dto.getPage(), dto.getSize());
        List<SinglePageVO> singlePageList = baseMapper.getSinglePageList(dto);
        return new PageBean<>(singlePageList);
    }

    @Override
    public JsonResult saveSinglePage(SinglePageVO vo) {
        SysSinglePageEntity sysSinglePageEntity;
        if (vo.getId() != null) {
            sysSinglePageEntity = baseMapper.selectById(vo.getId());
            if (sysSinglePageEntity == null) {
                return JsonResult.fail("id错误");
            }
            sysSinglePageEntity.setCode(vo.getCode());
            sysSinglePageEntity.setContent(vo.getContent());
            sysSinglePageEntity.setCoverUrl(vo.getCoverUrl());
            sysSinglePageEntity.setStatus(vo.getStatus());
            sysSinglePageEntity.setType(vo.getType());
            sysSinglePageEntity.setTitle(vo.getTitle());
        } else {
            sysSinglePageEntity = new SysSinglePageEntity();
            BeanUtils.copyProperties(vo, sysSinglePageEntity);
        }
        saveOrUpdate(sysSinglePageEntity);
        log.info("新增或修改单页面成功，id：{}", sysSinglePageEntity.getId());
        return JsonResult.success();
    }
}