package com.home.modules.generator.service.impl;

import com.github.pagehelper.PageHelper;
import com.home.common.utils.*;
import com.home.common.vo.DatadictVO;
import com.home.modules.generator.vo.OpeningNoticeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.home.modules.generator.dao.OpeningNoticeDao;
import com.home.modules.generator.entity.OpeningNoticeEntity;
import com.home.modules.generator.service.OpeningNoticeService;

@Slf4j
@Service("openingNoticeService")
public class OpeningNoticeServiceImpl extends ServiceImpl<OpeningNoticeDao, OpeningNoticeEntity> implements OpeningNoticeService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OpeningNoticeEntity> page = this.page(
                new Query<OpeningNoticeEntity>().getPage(params),
                new QueryWrapper<OpeningNoticeEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public OpeningNoticeVO getOpeningNotice() {
        QueryWrapper<OpeningNoticeEntity> queryWrapper = new QueryWrapper<OpeningNoticeEntity>().eq("status", Constant.Status.ENABLE.getType());
        OpeningNoticeEntity openingNoticeEntity = baseMapper.selectOne(queryWrapper);
        OpeningNoticeVO openingNoticeVO = new OpeningNoticeVO();
        BeanUtils.copyProperties(openingNoticeEntity, openingNoticeVO);
        return openingNoticeVO;
    }

    @Override
    public PageBean<OpeningNoticeVO> getOpeningNoticeList(PageReq pageReq) {
        PageHelper.startPage(pageReq.getPage(), pageReq.getSize());
        List<OpeningNoticeVO> openingNoticeList = baseMapper.getOpeningNoticeList();
        PageBean<OpeningNoticeVO> pageBean = new PageBean<>(openingNoticeList);
        return pageBean;
    }

    @Override
    public JsonResult updateOpeningNoticeStatus() {
        baseMapper.updateOpeningNoticeStatus();
        return JsonResult.success();
    }

}