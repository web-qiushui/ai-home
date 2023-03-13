package com.home.modules.generator.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.home.common.utils.PageUtils;
import com.home.common.utils.Query;

import com.home.modules.generator.dao.EvaluationCommentLikeDao;
import com.home.modules.generator.entity.EvaluationCommentLikeEntity;
import com.home.modules.generator.service.EvaluationCommentLikeService;

@Slf4j
@Service("evaluationCommentLikeService")
public class EvaluationCommentLikeServiceImpl extends ServiceImpl<EvaluationCommentLikeDao, EvaluationCommentLikeEntity> implements EvaluationCommentLikeService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<EvaluationCommentLikeEntity> page = this.page(
                new Query<EvaluationCommentLikeEntity>().getPage(params),
                new QueryWrapper<EvaluationCommentLikeEntity>()
        );

        return new PageUtils(page);
    }

}