package com.home.modules.generator.service.impl;

import com.github.pagehelper.PageHelper;
import com.home.common.utils.PageBean;
import com.home.common.utils.PageReq;
import com.home.modules.generator.dto.ConsultantCommentDTO;
import com.home.modules.generator.vo.ConsultantCommentVO;
import com.home.modules.generator.vo.EvaluationCommentVO;
import com.home.modules.generator.vo.StarAndCountVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.home.common.utils.PageUtils;
import com.home.common.utils.Query;

import com.home.modules.generator.dao.ConsultantCommentDao;
import com.home.modules.generator.entity.ConsultantCommentEntity;
import com.home.modules.generator.service.ConsultantCommentService;

@Slf4j
@Service("consultantCommentService")
public class ConsultantCommentServiceImpl extends ServiceImpl<ConsultantCommentDao, ConsultantCommentEntity> implements ConsultantCommentService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ConsultantCommentEntity> page = this.page(
                new Query<ConsultantCommentEntity>().getPage(params),
                new QueryWrapper<ConsultantCommentEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageBean<ConsultantCommentVO> getConsultantCommentList(ConsultantCommentDTO dto) {
        PageHelper.startPage(dto.getPage(), dto.getSize());
        dto.setOrderBy(dto.getDesc(), dto.getAsc());
        List<ConsultantCommentVO> demandList = baseMapper.getConsultantCommentList(dto);
        PageBean<ConsultantCommentVO> pageBean = new PageBean<>(demandList);
        return pageBean;
    }

    @Override
    public PageBean<ConsultantCommentVO> getConsultantComment(ConsultantCommentDTO dto) {
        PageHelper.startPage(dto.getPage(), dto.getSize());
        dto.setOrderBy(dto.getDesc(), dto.getAsc());
        List<ConsultantCommentVO> demandList = baseMapper.getConsultantComment(dto.getConsultantId());
        PageBean<ConsultantCommentVO> pageBean = new PageBean<>(demandList);
        return pageBean;
    }

    @Override
    public StarAndCountVO getStarAndCount(Integer consultentId) {
        return baseMapper.getStarAndCount(consultentId);
    }

}