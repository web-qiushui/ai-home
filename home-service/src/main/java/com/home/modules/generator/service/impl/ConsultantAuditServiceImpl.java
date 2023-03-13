package com.home.modules.generator.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.home.common.utils.PageUtils;
import com.home.common.utils.Query;

import com.home.modules.generator.dao.ConsultantAuditDao;
import com.home.modules.generator.entity.ConsultantAuditEntity;
import com.home.modules.generator.service.ConsultantAuditService;

@Slf4j
@Service("consultantAuditService")
public class ConsultantAuditServiceImpl extends ServiceImpl<ConsultantAuditDao, ConsultantAuditEntity> implements ConsultantAuditService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ConsultantAuditEntity> page = this.page(
                new Query<ConsultantAuditEntity>().getPage(params),
                new QueryWrapper<ConsultantAuditEntity>()
        );

        return new PageUtils(page);
    }

}