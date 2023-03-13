package com.home.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.home.common.utils.PageUtils;
import com.home.modules.generator.entity.LoginEntity;
import com.home.modules.generator.vo.LoginStatisticslVO;

import java.util.Map;

/**
 * 用户登录记录表
 *
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-07-22 22:25:48
 */
public interface LoginService extends IService<LoginEntity> {

    PageUtils queryPage(Map<String, Object> params);

    LoginStatisticslVO getLoginStatistics();


}

