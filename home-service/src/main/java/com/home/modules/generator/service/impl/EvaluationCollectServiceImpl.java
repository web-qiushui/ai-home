package com.home.modules.generator.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.home.common.utils.*;
import com.home.modules.generator.dao.EvaluationCollectDao;
import com.home.modules.generator.entity.EvaluationCollectEntity;
import com.home.modules.generator.service.EvaluationCollectService;
import com.home.modules.generator.vo.EvaluationVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service("evaluationCollectService")
public class EvaluationCollectServiceImpl extends ServiceImpl<EvaluationCollectDao, EvaluationCollectEntity> implements EvaluationCollectService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<EvaluationCollectEntity> page = this.page(
                new Query<EvaluationCollectEntity>().getPage(params),
                new QueryWrapper<EvaluationCollectEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageBean<EvaluationVO> getEvaluationCollectList(PageReq page, Integer userId) {
        PageHelper.startPage(page.getPage(), page.getSize());
        List<EvaluationVO> evaluationCollectList = baseMapper.getEvaluationCollectList(userId);
        PageBean<EvaluationVO> pageBean = new PageBean<>(evaluationCollectList);
        return pageBean;
    }

    @Override
    public Integer getEvaluationCollectSize(Integer userId) {
        QueryWrapper<EvaluationCollectEntity> queryWrapper = new QueryWrapper<EvaluationCollectEntity>()
                .eq("home_user_id", userId)
                .eq("status", Constant.Status.ENABLE.getType());
        return baseMapper.selectCount(queryWrapper);
    }

}