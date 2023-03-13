package com.home.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.home.common.utils.PageUtils;
import com.home.modules.generator.entity.EvaluationLikeEntity;

import java.util.Map;

/**
 * 测评点赞表
 *
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-11 21:12:12
 */
public interface EvaluationLikeService extends IService<EvaluationLikeEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

