package com.home.modules.generator.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.home.common.constant.config.KingKongConfig;
import com.home.common.utils.*;
import com.home.modules.generator.dto.EvaluationDTO;
import com.home.modules.generator.dto.SaveEvaluationDTO;
import com.home.modules.generator.entity.EvaluationCollectEntity;
import com.home.modules.generator.entity.EvaluationDetailsEntity;
import com.home.modules.generator.entity.EvaluationLikeEntity;
import com.home.modules.generator.service.EvaluationCollectService;
import com.home.modules.generator.service.EvaluationDetailsService;
import com.home.modules.generator.service.EvaluationLikeService;
import com.home.modules.generator.vo.EvaluationListVO;
import com.home.modules.generator.vo.EvaluationVO;
import com.home.modules.sys.service.SysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.home.modules.generator.dao.EvaluationDao;
import com.home.modules.generator.entity.EvaluationEntity;
import com.home.modules.generator.service.EvaluationService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Slf4j
@Service("evaluationService")
public class EvaluationServiceImpl extends ServiceImpl<EvaluationDao, EvaluationEntity> implements EvaluationService {

    @Resource
    private SysConfigService sysConfigService;
    @Resource
    private EvaluationService evaluationService;
    @Resource
    private EvaluationDetailsService evaluationDetailsService;
    @Resource
    private EvaluationLikeService evaluationLikeService;
    @Resource
    private EvaluationCollectService evaluationCollectService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<EvaluationEntity> page = this.page(
                new Query<EvaluationEntity>().getPage(params),
                new QueryWrapper<EvaluationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageBean<EvaluationVO> getEvaluationList(EvaluationDTO dto) {
        PageHelper.startPage(dto.getPage(), dto.getSize());
        dto.setOrderBy(dto.getDesc(), dto.getAsc());
        List<EvaluationVO> evaluationList = baseMapper.getEvaluationList(dto);
        PageBean<EvaluationVO> pageBean = new PageBean<>(evaluationList);
        evaluationList.forEach(vo -> vo.setLabel(JSONObject.parseArray(vo.getLabelStr(), String.class)));
        pageBean.setList(evaluationList);
        return pageBean;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult saveEvaluation(SaveEvaluationDTO dto) {
        EvaluationEntity evaluationEntity;
        EvaluationDetailsEntity evaluationDetailsEntity;
        if (dto.getId() != null) {
            evaluationEntity = evaluationService.getById(dto.getId());
            evaluationDetailsEntity = evaluationDetailsService.getById(dto.getId());
            if (evaluationEntity == null) {
                return JsonResult.fail("测评Id错误");
            }
            evaluationEntity.setTitle(dto.getTitle());
            evaluationEntity.setAuthor(dto.getAuthor());
            evaluationEntity.setMainImage(dto.getMainImage());
            if (dto.getLabel() != null) {
                evaluationEntity.setLabel(JSON.toJSONString(dto.getLabel()));
            }
            evaluationEntity.setPrice(dto.getPrice());
            evaluationEntity.setDescription(dto.getDescription());
            evaluationEntity.setOriginal(dto.getOriginal());
            evaluationEntity.setThePublicName(dto.getThePublicName());
            evaluationEntity.setArea(dto.getArea());
            evaluationEntity.setRegion(dto.getRegion());
            evaluationEntity.setPrice(dto.getPrice());
        } else {
            evaluationEntity = new EvaluationEntity();
            evaluationDetailsEntity = new EvaluationDetailsEntity();
            BeanUtils.copyProperties(dto, evaluationEntity);
            evaluationEntity.setLabel(JSON.toJSONString(dto.getLabel()));
            evaluationEntity.setSysUserId(dto.getSysUserId());
            evaluationEntity.setHomeUserId(dto.getUserId());
            evaluationEntity.setCommentCount(0);
            evaluationEntity.setCollectCount(0);
            evaluationEntity.setForwardCount(0);
            evaluationEntity.setLikeCount(0);
        }
        evaluationService.saveOrUpdate(evaluationEntity);
        evaluationDetailsEntity.setId(evaluationEntity.getId());
        evaluationDetailsEntity.setContent(dto.getContent());
        evaluationDetailsService.saveOrUpdate(evaluationDetailsEntity);
        log.info("新增或修改测评成功，测评Id：{}", evaluationEntity.getId());
        return JsonResult.success();
    }

    @Override
    public EvaluationVO getEvaluationInfo(Integer id, Integer userId) {
        EvaluationEntity evaluationEntity = baseMapper.selectById(id);
        EvaluationDetailsEntity evaluationDetailsEntity = evaluationDetailsService.getById(id);
        EvaluationVO vo = new EvaluationVO();
        BeanUtils.copyProperties(evaluationEntity, vo);
        List<String> label = JSONObject.parseArray(evaluationEntity.getLabel(), String.class);
        vo.setLabel(label);
        vo.setIsCollect(Constant.Status.DISABLE.getType());
        vo.setIsLike(Constant.Status.DISABLE.getType());
        vo.setContent(evaluationDetailsEntity.getContent());
        if (userId != null) {
            QueryWrapper<EvaluationLikeEntity> queryWrapper = new QueryWrapper<EvaluationLikeEntity>()
                    .eq("home_evaluation_id", id)
                    .eq("home_user_id", userId)
                    .eq("status", Constant.Status.ENABLE.getType());
            int likeCount = evaluationLikeService.count(queryWrapper);
            QueryWrapper<EvaluationCollectEntity> wrapper = new QueryWrapper<EvaluationCollectEntity>()
                    .eq("home_evaluation_id", id)
                    .eq("home_user_id", userId)
                    .eq("status", Constant.Status.ENABLE.getType());
            int collectCount = evaluationCollectService.count(wrapper);
            if (collectCount > 0) {
                vo.setIsCollect(Constant.Status.ENABLE.getType());
            }
            if (likeCount > 0) {
                vo.setIsLike(Constant.Status.ENABLE.getType());
            }
        }
        return vo;
    }

    @Override
    public List<EvaluationListVO> getRecommendList(Integer id) {
        EvaluationEntity evaluationEntity = baseMapper.selectById(id);
        List<String> label = JSON.parseArray(evaluationEntity.getLabel(), String.class);
        EvaluationDTO dto = EvaluationDTO.builder()
                .id(id)
                .type(1)
                .label(label)
                .status(Constant.Status.ENABLE.getType())
                .build();
        List<EvaluationListVO> list = baseMapper.getRecommendList(dto);
        if (list.size() == 0) {
            dto.setLabel(null);
            list = baseMapper.getRecommendList(dto);
        }
        return list;
    }

    @Override
    public PageBean<EvaluationListVO> getMainPageInfo(EvaluationDTO dto) {
        PageHelper.startPage(dto.getPage(), dto.getSize());
        dto.setOrderBy(dto.getDesc(), dto.getAsc());
        dto.setStatus(Constant.Status.ENABLE.getType());
        List<EvaluationListVO> evaluationList = baseMapper.getMainPageInfo(dto);
        PageBean<EvaluationListVO> pageBean = new PageBean<>(evaluationList);
        evaluationList.forEach(vo -> vo.setLabel(JSONObject.parseArray(vo.getLabelStr(), String.class)));
        pageBean.setList(evaluationList);
        return pageBean;
    }

    @Override
    public List<EvaluationListVO> getKingKongList(String type) {
        KingKongConfig kingKongConfig = sysConfigService.getKingKongConfig(type);
        List<EvaluationListVO> list = new ArrayList<>();
        if (!(kingKongConfig.getEvaluationIds() != null && kingKongConfig.getEvaluationIds().size() > 0)) {
            return null;
        }
        List<EvaluationEntity> evaluationList = baseMapper.selectBatchIds(kingKongConfig.getEvaluationIds());
        evaluationList.forEach(evaluationEntity -> {
            EvaluationListVO vo = new EvaluationListVO();
            BeanUtils.copyProperties(evaluationEntity, vo);
            List<String> label = JSONObject.parseArray(evaluationEntity.getLabel(), String.class);
            vo.setLabel(label);
            vo.setContent(evaluationEntity.getDescription());
            list.add(vo);
        });
        return list;
    }

    @Override
    public PageBean<EvaluationListVO> get24hNews(EvaluationDTO dto) {
        PageHelper.startPage(dto.getPage(), dto.getSize());
        dto.setStatus(Constant.Status.ENABLE.getType());
        List<EvaluationListVO> evaluationList = baseMapper.get24hNews(dto);
        PageBean<EvaluationListVO> pageBean = new PageBean<>(evaluationList);
        List<EvaluationLikeEntity> entityList = null;
        if (dto.getUserId() != null && evaluationList.size() > 0) {
            List<Integer> ids = evaluationList.stream().map(EvaluationListVO::getId).collect(Collectors.toList());
            LambdaQueryWrapper<EvaluationLikeEntity> queryWrapper = Wrappers.<EvaluationLikeEntity>lambdaQuery()
                    .in(EvaluationLikeEntity::getHomeEvaluationId, ids)
                    .eq(EvaluationLikeEntity::getHomeUserId, dto.getUserId())
                    .eq(EvaluationLikeEntity::getStatus, Constant.Status.ENABLE.getType());
            entityList = evaluationLikeService.list(queryWrapper);
        }
        for (EvaluationListVO vo : evaluationList) {
            vo.setIsLike(Constant.Status.DISABLE.getType());
            if (entityList != null) {
                entityList.forEach(evaluationLikeEntity -> {
                    if (vo.getId().equals(evaluationLikeEntity.getHomeEvaluationId())) {
                        vo.setIsLike(Constant.Status.ENABLE.getType());
                    }
                });
            }
        }
        pageBean.setList(evaluationList);
        return pageBean;
    }
}