package com.home.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.home.common.utils.PageUtils;
import com.home.modules.generator.entity.EvaluationCommentLikeEntity;

import java.util.Map;

/**
 * 测评评论表
 *
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-23 21:19:16
 */
public interface EvaluationCommentLikeService extends IService<EvaluationCommentLikeEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

