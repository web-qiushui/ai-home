package com.home.modules.sys.service.impl;

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.home.common.utils.Constant;
import com.home.common.utils.JsonResult;
import com.home.common.vo.DatadictVO;
import com.home.modules.app.entity.UserEntity;
import com.home.modules.app.service.UserService;
import com.home.modules.generator.dao.StatisticsDao;
import com.home.modules.generator.dto.*;
import com.home.modules.generator.entity.*;
import com.home.modules.generator.service.*;
import com.home.modules.generator.vo.OpeningNoticeVO;
import com.home.modules.generator.vo.StatisticslVO;
import com.home.modules.sys.entity.SysUserEntity;
import com.home.modules.sys.service.SysApiService;
import com.home.modules.sys.service.SysDatadictService;
import com.home.modules.sys.service.SysUserRoleService;
import com.home.modules.sys.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Slf4j
@Service("sysApiService")

public class SysApiServiceImpl implements SysApiService {

    @Resource
    private UserService userService;
    @Resource
    private EvaluationCommentService evaluationCommentService;
    @Resource
    private CarouselService carouselService;
    @Resource
    private ConsultantService consultantService;
    @Resource
    private ConsultantAuditService consultantAuditService;
    @Resource
    private SysUserRoleService sysUserRoleService;
    @Resource
    private SysUserService sysUserService;
    @Resource
    private OpeningNoticeService openingNoticeService;
    @Resource
    private SysDatadictService sysDatadictService;
    @Resource
    private DemandService demandService;
    @Resource
    private StatisticsDao statisticsDao;

    @Override
    public JsonResult auditEvaluationComment(EvaluationCommentDTO dto, Integer sysUserId) {
        EvaluationCommentEntity commentEntity = evaluationCommentService.getById(dto.getEvaluationId());
        if (commentEntity == null) {
            return JsonResult.fail("评论Id错误");
        }
        if (!Constant.AuditStatus.AUDIT.equals(commentEntity.getStatus())) {
            return JsonResult.fail("审核已完成");
        }
        commentEntity.setOperateId(sysUserId);
        commentEntity.setRemark(dto.getRemark());
        commentEntity.setStatus(dto.getStatus());
        evaluationCommentService.updateById(commentEntity);
        return JsonResult.success();
    }

    @Override
    public JsonResult saveCarousel(SaveCarouselVO dto) {
        CarouselEntity carouselEntity;
        if (dto.getId() != null) {
            carouselEntity = carouselService.getById(dto.getId());
            if (StringUtils.isNotEmpty(dto.getImageUrl())) {
                carouselEntity.setImageUrl(dto.getImageUrl());
            }
            if (StringUtils.isNotEmpty(dto.getPagePath())) {
                carouselEntity.setPagePath(dto.getPagePath());
            }
            if (StringUtils.isNotEmpty(dto.getDescription())) {
                carouselEntity.setDescription(dto.getDescription());
            }
            if (dto.getStatus() != null) {
                carouselEntity.setStatus(dto.getStatus());
            }
            if (dto.getStatus() != null) {
                carouselEntity.setSort(dto.getSort());
            }
        } else {
            carouselEntity = CarouselEntity.builder()
                    .description(dto.getDescription())
                    .imageUrl(dto.getImageUrl())
                    .pagePath(dto.getPagePath())
                    .sort(dto.getSort())
                    .status(Constant.Status.ENABLE.getType())
                    .build();
        }
        carouselService.saveOrUpdate(carouselEntity);
        log.info("修改或新增轮播图成功，id：{}", carouselEntity.getId());
        return JsonResult.success();
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public JsonResult auditConsultant(AuditConsultantDTO dto) {
        ConsultantEntity consultantEntity = consultantService.getById(dto.getId());
        if (consultantEntity == null) {
            return JsonResult.fail("顾问Id错误");
        }
        if (!Constant.ConsultantStatus.AUDIT.equals(consultantEntity.getStatus())) {
            return JsonResult.fail("审核完成无法重复审核");
        }
        LambdaQueryWrapper<ConsultantAuditEntity> queryWrapper = Wrappers.<ConsultantAuditEntity>lambdaQuery()
                .eq(ConsultantAuditEntity::getHomeConsultantId, dto.getId())
                .orderByDesc(ConsultantAuditEntity::getCreateTime)
                .last(" LIMIT 1");
        ConsultantAuditEntity consultantAuditEntity = consultantAuditService.getOne(queryWrapper);
        if (consultantAuditEntity == null) {
            return JsonResult.fail("无审核数据");
        }
        ConsultantAuditEntity entity = new ConsultantAuditEntity();
        entity.setOperateId(dto.getOperateId().intValue());
        entity.setOperateTime(new Date());
        entity.setHomeConsultantId(consultantEntity.getId());
        entity.setRemark(dto.getRemark());
        List<Long> longs = sysUserRoleService.queryRoleIdList(dto.getOperateId());
        if (Constant.ConsultantAuditType.APPLICATION.equals(consultantAuditEntity.getType())) {
            if (dto.getFirmId() == null) {
                return JsonResult.fail("企业Id不能为空");
            }
            if (!(longs.contains(Constant.Role.SYSTEM) || longs.contains(Constant.Role.ADMIN_SYSTEM))) {
                return JsonResult.fail("权限不足");
            }
            List<Long> roleIdList = sysUserRoleService.queryRoleIdList(dto.getFirmId());
            if (!roleIdList.contains(Constant.Role.FIRM_SYSTEM)) {
                return JsonResult.fail("企业账户错误");
            }
            entity.setType(Constant.ConsultantAuditType.SYSTEM_AUDIT);
            if (Constant.AuditStatus.SUCCESS.equals(dto.getStatus())) {
                consultantEntity.setFirmId(dto.getFirmId().intValue());
                entity.setStatus(Constant.AuditStatus.SUCCESS);
            } else {
                consultantEntity.setStatus(Constant.ConsultantStatus.FAIL);
                entity.setStatus(Constant.AuditStatus.FAIL);
            }
        } else if (Constant.ConsultantAuditType.SYSTEM_AUDIT.equals(consultantAuditEntity.getType())) {
            if (!longs.contains(Constant.Role.FIRM_SYSTEM)) {
                return JsonResult.fail("权限不足");
            }
            entity.setType(Constant.ConsultantAuditType.FIRM_AUDIT);
            if (Constant.AuditStatus.SUCCESS.equals(dto.getStatus())) {
                consultantEntity.setStatus(Constant.ConsultantStatus.SUCCESS);
                entity.setStatus(Constant.AuditStatus.SUCCESS);
                SysUserEntity sysUserEntity = sysUserService.queryByAppUserId(consultantEntity.getHomeUserId());
                if (sysUserEntity == null) {
                    sysUserEntity = new SysUserEntity();
                    sysUserEntity.setAppUserId(consultantEntity.getHomeUserId());
                    sysUserEntity.setUsername(consultantEntity.getPhone());
                    sysUserEntity.setMobile(consultantEntity.getPhone());
                    sysUserEntity.setName(consultantEntity.getName());
                    sysUserEntity.setPassword(consultantEntity.getPhone().substring(5));
                    sysUserEntity.setRoleIdList(Collections.singletonList(Constant.Role.HOME));
                    sysUserEntity.setCreateUserId(dto.getOperateId());
                    sysUserService.saveAppUser(sysUserEntity);
                }
                UserEntity userEntity = userService.getById(consultantEntity.getHomeUserId());
                userEntity.setRole(Constant.AppRole.HOME);
                userEntity.setName(consultantEntity.getName());
                userEntity.setIdCard(consultantEntity.getIdCard());
                userEntity.setPhone(consultantEntity.getPhone());
                userService.updateById(userEntity);
            } else {
                consultantEntity.setStatus(Constant.ConsultantStatus.FAIL);
                entity.setStatus(Constant.AuditStatus.FAIL);
            }
        }
        consultantService.updateById(consultantEntity);
        consultantAuditService.save(entity);
        return JsonResult.success();
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public JsonResult saveConsultant(SaveConsultantDTO dto, Long sysUserId) {
        List<Long> longs = sysUserRoleService.queryRoleIdList(sysUserId);
        if (!(longs.contains(Constant.Role.SYSTEM) || longs.contains(Constant.Role.ADMIN_SYSTEM))) {
            return JsonResult.fail("权限不足");
        }
        UserEntity userEntity = userService.getById(dto.getUserId());
        if (userEntity == null) {
            return JsonResult.fail("用户错误");
        }
        LambdaQueryWrapper<ConsultantEntity> queryWrapper = Wrappers.<ConsultantEntity>lambdaQuery()
                .eq(ConsultantEntity::getHomeUserId, dto.getUserId());
        ConsultantEntity entity = consultantService.getOne(queryWrapper);
        if (entity == null) {
            entity = ConsultantEntity.builder()
                    .status(Constant.ConsultantStatus.SUCCESS)
                    .type(Constant.AppRole.AI)
                    .star(0)
                    .advisoryCount(0)
                    .buyHomeCount(0)
                    .phone(dto.getPhone())
                    .idCard(dto.getIdCard())
                    .name(dto.getName())
                    .avatar(dto.getAvatar())
                    .homeUserId(dto.getUserId())
                    .project(JSON.toJSONString(dto.getProject()))
                    .build();
            SysUserEntity sysUserEntity = sysUserService.queryByAppUserId(dto.getUserId());
            if (sysUserEntity == null) {
                sysUserEntity = new SysUserEntity();
                sysUserEntity.setStatus(Constant.Status.ENABLE.getType());
                sysUserEntity.setAppUserId(entity.getHomeUserId());
                sysUserEntity.setUsername(entity.getPhone());
                sysUserEntity.setMobile(entity.getPhone());
                sysUserEntity.setName(entity.getName());
                sysUserEntity.setPassword(entity.getPhone().substring(5));
                sysUserEntity.setRoleIdList(Collections.singletonList(Constant.Role.AI));
                sysUserEntity.setCreateUserId(sysUserId);
                sysUserService.saveAppUser(sysUserEntity);
            }
        } else {
            entity.setPhone(dto.getPhone());
            entity.setIdCard(dto.getIdCard());
            entity.setName(dto.getName());
            entity.setAvatar(dto.getAvatar());
            entity.setProject(JSON.toJSONString(dto.getProject()));
        }
        userEntity.setRole(Constant.AppRole.AI);
        userEntity.setName(dto.getName());
        userEntity.setIdCard(dto.getIdCard());
        userEntity.setPhone(dto.getPhone());
        userService.updateById(userEntity);
        consultantService.saveOrUpdate(entity);
        log.info("顾问新增或更新成功，用户Id：{}", dto.getUserId());
        return JsonResult.success();
    }

    @Override
    public JsonResult saveOpeningNotice(OpeningNoticeVO dto) {
        OpeningNoticeEntity entity;
        if (dto.getId() == null) {
            entity = OpeningNoticeEntity.builder()
                    .imageUrl(dto.getImageUrl())
                    .description(dto.getDescription())
                    .status(dto.getStatus())
                    .build();
        } else {
            entity = openingNoticeService.getById(dto.getId());
            if (entity == null) {
                return JsonResult.fail("开盘公告为空");
            }
            if (StringUtils.isNotEmpty(dto.getDescription())) {
                entity.setDescription(dto.getDescription());
            }
            if (StringUtils.isNotEmpty(dto.getImageUrl())) {
                entity.setImageUrl(dto.getImageUrl());
            }
            if (dto.getStatus() != null) {
                entity.setStatus(dto.getStatus());
            }
        }
        if (Constant.Status.ENABLE.getType().equals(dto.getStatus())) {
            openingNoticeService.updateOpeningNoticeStatus();
        }
        openingNoticeService.saveOrUpdate(entity);
        log.info("新增或修改开盘公告成功");
        return JsonResult.success();
    }

    @Override
    public JsonResult saveDemand(DemandDTO dto) {
        DatadictVO datadict = sysDatadictService.getDatadict(Constant.Datadict.LABEL_SIZE);
        if (dto.getLabel().size() > Convert.toInt(datadict.getValue())) {
            return JsonResult.fail("标签最多选择" + datadict.getValue() + "个");
        }
        DemandEntity demandEntity = demandService.getById(dto.getId());
        demandEntity.setArea(dto.getArea());
        demandEntity.setLabel(JSON.toJSONString(dto.getLabel()));
        demandEntity.setRegion(dto.getRegion());
        demandEntity.setExplanation(dto.getExplanation());
        demandEntity.setPrice(dto.getPrice());
        demandEntity.setIntentions(dto.getIntentions());
        demandEntity.setPhone(dto.getPhone());
        demandService.updateById(demandEntity);
        log.info("购房需求添加或修改成功");
        return JsonResult.success();
    }

    @Override
    public StatisticslVO getStatistics(String type) {
        Integer onlineCount = statisticsDao.getOnlineCount(type);
        Integer forwardCount = statisticsDao.getForwardCount(type);
        Integer likeCount = statisticsDao.getLikeCount(type);
        Integer commentCount = statisticsDao.getCommentCount(type);
        Integer collectCount = statisticsDao.getCollectCount(type);
        Integer demandCount = statisticsDao.getDemandCount(type);
        Integer evaluationCount = statisticsDao.getEvaluationCount(type);
        Integer newsCount = statisticsDao.getNewsCount(type);
        StatisticslVO vo = new StatisticslVO();
        vo.setOnlineCount(onlineCount == null ? 0 : onlineCount);
        vo.setForwardCount(forwardCount == null ? 0 : forwardCount);
        vo.setLikeCount(likeCount == null ? 0 : likeCount);
        vo.setCommentCount(commentCount == null ? 0 : commentCount);
        vo.setCollectCount(collectCount == null ? 0 : collectCount);
        vo.setDemandCount(demandCount == null ? 0 : demandCount);
        vo.setEvaluationCount(evaluationCount == null ? 0 : evaluationCount);
        vo.setNewsCount(newsCount == null ? 0 : newsCount);
        return vo;
    }
}
