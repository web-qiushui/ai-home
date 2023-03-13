package com.home.modules.sys.service.impl;

import com.github.pagehelper.PageHelper;
import com.home.common.dto.DatadictDTO;
import com.home.common.utils.*;
import com.home.common.vo.DatadictVO;
import com.home.modules.generator.vo.ConsultantVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.home.modules.sys.dao.SysDatadictDao;
import com.home.modules.sys.entity.SysDatadictEntity;
import com.home.modules.sys.service.SysDatadictService;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service("datadictService")
public class SysDatadictServiceImpl extends ServiceImpl<SysDatadictDao, SysDatadictEntity> implements SysDatadictService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SysDatadictEntity> page = this.page(
                new Query<SysDatadictEntity>().getPage(params),
                new QueryWrapper<SysDatadictEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public DatadictVO getDatadict(String type) {
        QueryWrapper<SysDatadictEntity> queryWrapper = new QueryWrapper<SysDatadictEntity>().eq("type", type);
        SysDatadictEntity datadictEntity = baseMapper.selectOne(queryWrapper);
        DatadictVO vo = new DatadictVO();
        BeanUtils.copyProperties(datadictEntity, vo);
        return vo;
    }

    @Override
    public List<DatadictVO> getDatadict() {
        return baseMapper.getDatadict();
    }

    @Override
    public PageBean<DatadictVO> getDatadictList(DatadictDTO dto) {
        PageHelper.startPage(dto.getPage(), dto.getSize());
        List<DatadictVO> datadictList = baseMapper.getDatadictList(dto);
        PageBean<DatadictVO> pageBean = new PageBean<>(datadictList);
        return pageBean;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public JsonResult saveDatadict(DatadictVO dto, Integer userId) {
        SysDatadictEntity sysDatadictEntity;
        if (dto.getId() != null) {
            sysDatadictEntity = baseMapper.selectById(dto.getId());
            if (sysDatadictEntity == null) {
                return JsonResult.fail("数据不存在");
            }
            sysDatadictEntity.setType(dto.getType());
            sysDatadictEntity.setName(dto.getName());
            sysDatadictEntity.setValue(dto.getValue());
            sysDatadictEntity.setN1(dto.getN1());
            sysDatadictEntity.setN2(dto.getN2());
            sysDatadictEntity.setN3(dto.getN3());
            sysDatadictEntity.setN4(dto.getN4());
            sysDatadictEntity.setN5(dto.getN5());
            if (Constant.Status.ENABLE.getType().equals(dto.getStatus())) {
                sysDatadictEntity.setStatus(Constant.Status.ENABLE.getType());
            } else {
                sysDatadictEntity.setStatus(Constant.Status.DISABLE.getType());
            }
        } else {
            sysDatadictEntity = new SysDatadictEntity();
            BeanUtils.copyProperties(dto, sysDatadictEntity);
            sysDatadictEntity.setCreateBy(userId);
            sysDatadictEntity.setStatus(Constant.Status.ENABLE.getType());
        }
        this.saveOrUpdate(sysDatadictEntity);
        return JsonResult.success();
    }

}