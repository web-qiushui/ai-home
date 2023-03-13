package com.home.modules.generator.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.home.common.utils.PageBean;
import com.home.common.utils.PageReq;
import com.home.common.utils.PageUtils;
import com.home.common.utils.Query;
import com.home.modules.generator.dao.EvaluationCommentDao;
import com.home.modules.generator.dto.EvaluationCommentDTO;
import com.home.modules.generator.entity.EvaluationCommentEntity;
import com.home.modules.generator.service.EvaluationCommentService;
import com.home.modules.generator.vo.EvaluationCommentVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service("evaluationCommentService")
public class EvaluationCommentServiceImpl extends ServiceImpl<EvaluationCommentDao, EvaluationCommentEntity> implements EvaluationCommentService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<EvaluationCommentEntity> page = this.page(
                new Query<EvaluationCommentEntity>().getPage(params),
                new QueryWrapper<EvaluationCommentEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageBean<EvaluationCommentVO> getEvaluationCommentList(EvaluationCommentDTO dto) {
        PageHelper.startPage(dto.getPage(), dto.getSize());
        List<EvaluationCommentVO> demandList = baseMapper.getEvaluationCommentList(dto);
        PageBean<EvaluationCommentVO> pageBean = new PageBean<>(demandList);
        return pageBean;
    }

    @Override
    public PageBean<EvaluationCommentVO> getEvaluationComment(EvaluationCommentDTO dto) {
        PageHelper.startPage(dto.getPage(), dto.getSize());
        List<EvaluationCommentVO> demandList = baseMapper.getEvaluationComment(dto);
        PageBean<EvaluationCommentVO> pageBean = new PageBean<>(demandList);
        return pageBean;
    }

}