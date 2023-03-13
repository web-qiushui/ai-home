package com.home.modules.generator.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.home.common.utils.PageUtils;
import com.home.common.utils.Query;

import com.home.modules.generator.dao.EvaluationLikeDao;
import com.home.modules.generator.entity.EvaluationLikeEntity;
import com.home.modules.generator.service.EvaluationLikeService;

@Slf4j
@Service("evaluationLikeService")
public class EvaluationLikeServiceImpl extends ServiceImpl<EvaluationLikeDao, EvaluationLikeEntity> implements EvaluationLikeService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<EvaluationLikeEntity> page = this.page(
                new Query<EvaluationLikeEntity>().getPage(params),
                new QueryWrapper<EvaluationLikeEntity>()
        );

        return new PageUtils(page);
    }

}