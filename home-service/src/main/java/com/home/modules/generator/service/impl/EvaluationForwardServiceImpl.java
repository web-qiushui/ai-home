package com.home.modules.generator.service.impl;

import com.github.pagehelper.PageHelper;
import com.home.common.utils.PageBean;
import com.home.common.utils.PageReq;
import com.home.modules.generator.dto.EvaluationForwardDTO;
import com.home.modules.generator.vo.EvaluationVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.home.common.utils.PageUtils;
import com.home.common.utils.Query;

import com.home.modules.generator.dao.EvaluationForwardDao;
import com.home.modules.generator.entity.EvaluationForwardEntity;
import com.home.modules.generator.service.EvaluationForwardService;

@Slf4j
@Service("evaluationForwardService")
public class EvaluationForwardServiceImpl extends ServiceImpl<EvaluationForwardDao, EvaluationForwardEntity> implements EvaluationForwardService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<EvaluationForwardEntity> page = this.page(
                new Query<EvaluationForwardEntity>().getPage(params),
                new QueryWrapper<EvaluationForwardEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageBean<EvaluationVO> getEvaluationForwardList(EvaluationForwardDTO dto, Integer userId) {
        PageHelper.startPage(dto.getPage(), dto.getSize());
        List<EvaluationVO> evaluationCollectList = baseMapper.getEvaluationForwardList(dto.getSearch(), userId);
        PageBean<EvaluationVO> pageBean = new PageBean<>(evaluationCollectList);
        return pageBean;
    }

}