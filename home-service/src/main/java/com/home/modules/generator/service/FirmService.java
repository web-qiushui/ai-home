package com.home.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.home.common.utils.PageUtils;
import com.home.modules.generator.entity.FirmEntity;

import java.util.Map;

/**
 * 企业表
 *
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-28 22:14:45
 */
public interface FirmService extends IService<FirmEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

