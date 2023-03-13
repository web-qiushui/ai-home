package com.home.modules.generator.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.home.common.utils.PageUtils;
import com.home.common.utils.Query;

import com.home.modules.generator.dao.FirmDao;
import com.home.modules.generator.entity.FirmEntity;
import com.home.modules.generator.service.FirmService;

@Slf4j
@Service("firmService")
public class FirmServiceImpl extends ServiceImpl<FirmDao, FirmEntity> implements FirmService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<FirmEntity> page = this.page(
                new Query<FirmEntity>().getPage(params),
                new QueryWrapper<FirmEntity>()
        );

        return new PageUtils(page);
    }

}