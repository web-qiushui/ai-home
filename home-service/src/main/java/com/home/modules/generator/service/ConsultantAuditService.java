package com.home.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.home.common.utils.PageUtils;
import com.home.modules.generator.entity.ConsultantAuditEntity;

import java.util.Map;

/**
 * 顾问审核记录表
 *
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-21 23:41:37
 */
public interface ConsultantAuditService extends IService<ConsultantAuditEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

