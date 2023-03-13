package com.home.modules.app.service.impl;

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.home.common.constant.config.ChatCardConfig;
import com.home.common.dto.CaptchaDTO;
import com.home.common.utils.Constant;
import com.home.common.utils.JsonResult;
import com.home.common.utils.SocketCache;
import com.home.common.vo.DatadictVO;
import com.home.modules.app.service.AppApiService;
import com.home.modules.generator.dto.*;
import com.home.modules.generator.entity.*;
import com.home.modules.generator.service.*;
import com.home.modules.generator.vo.ConsultantVO;
import com.home.modules.generator.vo.DemandVO;
import com.home.modules.generator.vo.MineInfoVO;
import com.home.modules.generator.vo.StarAndCountVO;
import com.home.modules.sys.service.SysCaptchaService;
import com.home.modules.sys.service.SysConfigService;
import com.home.modules.sys.service.SysDatadictService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Slf4j
@Service("appApiService")
public class AppApiServiceImpl implements AppApiService {

    @Resource
    private DemandService demandService;
    @Resource
    private SysDatadictService sysDatadictService;
    @Resource
    private SysConfigService sysConfigService;
    @Resource
    private ConsultantCommentService consultantCommentService;
    @Resource
    private EvaluationCommentService evaluationCommentService;
    @Resource
    private ConsultantService consultantService;
    @Resource
    private EvaluationService evaluationService;
    @Resource
    private ConsultantAuditService consultantAuditService;
    @Resource
    private EvaluationForwardService evaluationForwardService;
    @Resource
    private EvaluationCollectService evaluationCollectService;
    @Resource
    private EvaluationLikeService evaluationLikeService;
    @Resource
    private EvaluationCommentLikeService evaluationCommentLikeService;
    @Resource
    private ConsultantUserService consultantUserService;
    @Resource
    private SysCaptchaService sysCaptchaService;
    @Resource
    private ChatRecordService chatRecordService;

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public JsonResult<DemandVO> saveDemand(DemandDTO dto) {
        DatadictVO datadict = sysDatadictService.getDatadict(Constant.Datadict.LABEL_SIZE);
        if (dto.getLabel().size() > Convert.toInt(datadict.getValue())) {
            return JsonResult.fail("标签最多选择" + datadict.getValue() + "个");
        }
        DemandEntity demandEntity =  DemandEntity.builder()
                .region(dto.getRegion())
                .price(dto.getPrice())
                .phone(dto.getPhone())
                .label(JSON.toJSONString((dto.getLabel())))
                .explanation(dto.getExplanation())
                .homeUserId(dto.getUserId())
                .area(dto.getArea()).build();
        if (dto.getId() != null) {
            demandEntity.setId(dto.getId());
        }
        demandService.saveOrUpdate(demandEntity);
        log.info("购房需求添加或修改成功");
        DemandVO vo = new DemandVO();
        BeanUtils.copyProperties(demandEntity, vo);
        vo.setLabel(dto.getLabel());
        return JsonResult.success(vo);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public JsonResult saveConsultant(SaveConsultantDTO dto) {
        CaptchaDTO captchaDTO = new CaptchaDTO();
        captchaDTO.setCode(dto.getCode());
        captchaDTO.setPhone(dto.getPhone());
        captchaDTO.setUuid(dto.getUuid());
        JsonResult jsonResult = sysCaptchaService.checkCaptcha(captchaDTO);
        if (!jsonResult.isSuccess()) {
            return jsonResult;
        }
        LambdaQueryWrapper<ConsultantEntity> queryWrapper = Wrappers.<ConsultantEntity>lambdaQuery()
                .eq(ConsultantEntity::getHomeUserId, dto.getUserId());
        ConsultantEntity entity = consultantService.getOne(queryWrapper);
        if (entity == null) {
            entity = ConsultantEntity.builder()
                    .status(Constant.ConsultantStatus.AUDIT)
                    .type(Constant.AppRole.HOME)
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
        } else {
            if (Constant.ConsultantStatus.AUDIT.equals(entity.getStatus())) {
                return JsonResult.fail("认证中，无法修改");
            }
            if (Constant.AppRole.HOME.equals(entity.getType())) {
                entity.setStatus(Constant.ConsultantStatus.AUDIT);
            }
            entity.setPhone(dto.getPhone());
            entity.setIdCard(dto.getIdCard());
            entity.setName(dto.getName());
            entity.setAvatar(dto.getAvatar());
            entity.setProject(JSON.toJSONString(dto.getProject()));
        }
        consultantService.saveOrUpdate(entity);
        if (Constant.AppRole.HOME.equals(entity.getType())) {
            ConsultantAuditEntity consultantAuditEntity = ConsultantAuditEntity.builder()
                    .homeConsultantId(entity.getId())
                    .operateId(dto.getUserId())
                    .operateTime(new Date())
                    .remark("申请")
                    .status(Constant.Status.ENABLE.getType())
                    .type(Constant.ConsultantAuditType.APPLICATION)
                    .build();
            consultantAuditService.save(consultantAuditEntity);
        }
        log.info("顾问申请或更新记录添加成功");
        return JsonResult.success();
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public JsonResult saveConsultantComment(SaveConsultantCommentDTO dto) {
        ConsultantEntity consultantEntity = consultantService.getById(dto.getConsultentId());
        if (consultantEntity == null) {
            return JsonResult.fail("顾问信息错误");
        }
        if (StringUtils.isNotEmpty(dto.getUuid())) {
            ChatRecordEntity chatRecordEntity = chatRecordService.getChatRecordByUuidAndType(dto.getUuid(), Constant.ChatRecordType.COMMENT);
            if (chatRecordEntity != null) {
                JSONObject jsonObject = JSON.parseObject(chatRecordEntity.getContent());
                if (jsonObject.get("status") == null ||jsonObject.getInteger("status") == 0) {
                    jsonObject.put("status", 1);
                    jsonObject.put("star", dto.getStar());
                    chatRecordEntity.setContent(jsonObject.toJSONString());
                    chatRecordService.updateById(chatRecordEntity);
                } else {
                    return JsonResult.fail("请勿重复评论");
                }
            }
        }
        DatadictVO datadict = sysDatadictService.getDatadict(Constant.Datadict.STAR_SIZE);
        if (datadict != null && StringUtils.isNotEmpty(datadict.getValue())) {
            if (dto.getStar() > Convert.toInt(datadict.getValue())) {
                return JsonResult.fail("打星数量不能大于" + Constant.Datadict.STAR_SIZE);
            }
        }
        ConsultantCommentEntity consultantCommentEntity = ConsultantCommentEntity.builder()
                .comment(dto.getComment())
                .homeConsultantId(dto.getConsultentId())
                .homeUserId(dto.getUserId())
                .star(dto.getStar())
                .build();
        consultantCommentService.save(consultantCommentEntity);
        log.info("新增顾问评价成功，顾问Id：{}，用户Id：{}", dto.getConsultentId(), dto.getUserId());
        StarAndCountVO starAndCount = consultantCommentService.getStarAndCount(dto.getConsultentId());
        int star = starAndCount.getStar()/starAndCount.getCountd();
        consultantEntity.setStar(star);
        consultantService.updateById(consultantEntity);
        return JsonResult.success();
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public JsonResult saveEvaluationComment(SaveEvaluationCommentDTO dto) {
        EvaluationEntity evaluationEntity = evaluationService.getById(dto.getEvaluationId());
        if (evaluationEntity == null) {
            return JsonResult.fail("测评Id错误");
        }
        EvaluationCommentEntity evaluationCommentEntity = EvaluationCommentEntity.builder()
                .comment(dto.getComment())
                .homeEvaluationId(dto.getEvaluationId())
                .homeUserId(dto.getUserId())
                .likeCount(0)
                .status(Constant.AuditStatus.AUDIT)
                .build();
        evaluationCommentService.save(evaluationCommentEntity);
        log.info("新增测评评论成功，测评Id：{}，用户Id：{}", dto.getEvaluationId(), dto.getUserId());
        evaluationEntity.setCommentCount(evaluationEntity.getCommentCount() + 1);
        evaluationService.updateById(evaluationEntity);
        return JsonResult.success();
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public JsonResult saveEvaluationForward(SaveEvaluationForwardDTO dto) {
        EvaluationEntity evaluationEntity = evaluationService.getById(dto.getEvaluationId());
        if (evaluationEntity == null) {
            return JsonResult.fail("测评Id错误");
        }
        QueryWrapper<EvaluationForwardEntity> queryWrapper = new QueryWrapper<EvaluationForwardEntity>()
                .eq("home_evaluation_id", dto.getEvaluationId()).eq("home_user_id", dto.getUserId());
        EvaluationForwardEntity entity = evaluationForwardService.getOne(queryWrapper);
        if (entity == null) {
            entity = EvaluationForwardEntity.builder()
                    .homeEvaluationId(dto.getEvaluationId())
                    .homeUserId(dto.getUserId())
                    .forwardCount(1)
                    .build();
        } else {
            entity.setForwardCount(entity.getForwardCount() + 1);
        }
        evaluationForwardService.saveOrUpdate(entity);
        log.info("新增或修改测评转发成功，测评Id：{}，用户Id：{}", dto.getEvaluationId(), dto.getUserId());
        evaluationEntity.setForwardCount(evaluationEntity.getForwardCount() + 1);
        evaluationService.updateById(evaluationEntity);
        return JsonResult.success();
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public JsonResult saveEvaluatioCollect(SaveEvaluatioCollectDTO dto) {
        EvaluationEntity evaluationEntity = evaluationService.getById(dto.getEvaluationId());
        if (evaluationEntity == null) {
            return JsonResult.fail("测评Id错误");
        }
        QueryWrapper<EvaluationCollectEntity> queryWrapper = new QueryWrapper<EvaluationCollectEntity>()
                .eq("home_evaluation_id", dto.getEvaluationId()).eq("home_user_id", dto.getUserId());
        EvaluationCollectEntity entity = evaluationCollectService.getOne(queryWrapper);
        if (entity == null) {
            entity = EvaluationCollectEntity.builder()
                    .homeEvaluationId(dto.getEvaluationId())
                    .homeUserId(dto.getUserId())
                    .status(Constant.Status.ENABLE.getType())
                    .build();
            evaluationEntity.setCollectCount(evaluationEntity.getCollectCount() + 1);
        } else {
            if (Constant.Status.ENABLE.getType().equals(entity.getStatus())) {
                entity.setStatus(Constant.Status.DISABLE.getType());
                evaluationEntity.setCollectCount(evaluationEntity.getCollectCount() - 1);
            } else {
                entity.setStatus(Constant.Status.ENABLE.getType());
                evaluationEntity.setCollectCount(evaluationEntity.getCollectCount() + 1);
            }
        }
        evaluationCollectService.saveOrUpdate(entity);
        log.info("新增测评收藏成功，测评Id：{}，用户Id：{}", dto.getEvaluationId(), dto.getUserId());
        evaluationService.updateById(evaluationEntity);
        return JsonResult.success();
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public JsonResult saveEvaluationLike(SaveEvaluationLikeDTO dto) {
        EvaluationEntity evaluationEntity = evaluationService.getById(dto.getEvaluationId());
        if (evaluationEntity == null) {
            return JsonResult.fail("测评Id错误");
        }
        QueryWrapper<EvaluationLikeEntity> queryWrapper = new QueryWrapper<EvaluationLikeEntity>()
                .eq("home_evaluation_id", dto.getEvaluationId()).eq("home_user_id", dto.getUserId());
        EvaluationLikeEntity entity = evaluationLikeService.getOne(queryWrapper);
        if (entity == null) {
            entity = EvaluationLikeEntity.builder()
                    .homeEvaluationId(dto.getEvaluationId())
                    .homeUserId(dto.getUserId())
                    .status(Constant.Status.ENABLE.getType())
                    .build();
            evaluationEntity.setLikeCount(evaluationEntity.getLikeCount() + 1);
        } else {
            if (Constant.Status.ENABLE.getType().equals(entity.getStatus())) {
                entity.setStatus(Constant.Status.DISABLE.getType());
                evaluationEntity.setLikeCount(evaluationEntity.getLikeCount() - 1);
            } else {
                entity.setStatus(Constant.Status.ENABLE.getType());
                evaluationEntity.setLikeCount(evaluationEntity.getLikeCount() + 1);
            }
        }
        evaluationLikeService.saveOrUpdate(entity);
        log.info("新增或修改测评点赞成功，测评Id：{}，用户Id：{}", dto.getEvaluationId(), dto.getUserId());
        evaluationService.updateById(evaluationEntity);
        return JsonResult.success();
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public JsonResult saveEvaluationCommentLike(SaveEvaluationCommentLikeDTO dto) {
        EvaluationCommentEntity evaluationCommentEntity = evaluationCommentService.getById(dto.getEvaluationCommentId());
        if (evaluationCommentEntity == null) {
            return JsonResult.fail("测评评论Id错误");
        }
        QueryWrapper<EvaluationCommentLikeEntity> queryWrapper = new QueryWrapper<EvaluationCommentLikeEntity>()
                .eq("home_evaluation_comment_id", dto.getEvaluationCommentId()).eq("home_user_id", dto.getUserId());
        EvaluationCommentLikeEntity entity = evaluationCommentLikeService.getOne(queryWrapper);
        if (entity == null) {
            entity = EvaluationCommentLikeEntity.builder()
                    .homeEvaluationCommentId(dto.getEvaluationCommentId())
                    .homeUserId(dto.getUserId())
                    .status(Constant.Status.ENABLE.getType())
                    .build();
            evaluationCommentEntity.setLikeCount(evaluationCommentEntity.getLikeCount() + 1);
        } else {
            if (Constant.Status.ENABLE.getType().equals(entity.getStatus())) {
                entity.setStatus(Constant.Status.DISABLE.getType());
                evaluationCommentEntity.setLikeCount(evaluationCommentEntity.getLikeCount() - 1);
            } else {
                entity.setStatus(Constant.Status.ENABLE.getType());
                evaluationCommentEntity.setLikeCount(evaluationCommentEntity.getLikeCount() + 1);
            }
        }
        evaluationCommentLikeService.saveOrUpdate(entity);
        log.info("新增测评评论点赞成功，测评Id：{}，用户Id：{}", dto.getEvaluationCommentId(), dto.getUserId());
        evaluationCommentService.updateById(evaluationCommentEntity);
        return JsonResult.success();
    }

    @Override
    public MineInfoVO getMineInfo(Integer userId) {
        MineInfoVO vo = new MineInfoVO();
        QueryWrapper<DemandEntity> demandWrapper = new QueryWrapper<DemandEntity>().eq("home_user_id", userId);
        int demandCount = demandService.count(demandWrapper);
        QueryWrapper<ConsultantUserEntity> consultantUserWrapper = new QueryWrapper<ConsultantUserEntity>().eq("home_user_id", userId);
        int consultantCount = consultantUserService.count(consultantUserWrapper);
        QueryWrapper<EvaluationForwardEntity> evaluationForwardWrapper = new QueryWrapper<EvaluationForwardEntity>().eq("home_user_id", userId);
        int evaluationForwardCount = evaluationForwardService.count(evaluationForwardWrapper);
        QueryWrapper<EvaluationCommentEntity> evaluationCommentWrapper = new QueryWrapper<EvaluationCommentEntity>().eq("home_user_id", userId);
        int evaluationCommentCount = evaluationCommentService.count(evaluationCommentWrapper);
        QueryWrapper<ConsultantCommentEntity> commentEntityQueryWrapper = new QueryWrapper<ConsultantCommentEntity>().eq("home_user_id", userId);
        int consultantCommentCount = consultantCommentService.count(commentEntityQueryWrapper);
        vo.setConsultantCount(consultantCount);
        vo.setDemandCount(demandCount);
        vo.setEvaluationForwardCount(evaluationForwardCount);
        vo.setEvaluationCommentCount(evaluationCommentCount);
        vo.setConsultantCommentCount(consultantCommentCount);
        return vo;
    }

    @Override
    public List<ChatCardConfig> getChatCard(Integer userId) {
        ConsultantVO consultantVO = consultantService.getConsultantByUserId(userId);
        List<ChatCardConfig> list = sysConfigService.getChatCardConfig(0, 1);
        if (consultantVO == null || !Constant.ConsultantStatus.SUCCESS.equals(consultantVO.getStatus())) {
            List<ChatCardConfig> chatCardConfigs = sysConfigService.getChatCardConfig(1, 1);
            list.addAll(chatCardConfigs);
        } else {
            if (Constant.AppRole.AI.equals(consultantVO.getType())) {
                List<ChatCardConfig> chatCardConfigs = sysConfigService.getChatCardConfig(2, 1);
                list.addAll(chatCardConfigs);
            }
            if (Constant.AppRole.HOME.equals(consultantVO.getType())) {
                List<ChatCardConfig> chatCardConfigs = sysConfigService.getChatCardConfig(3, 1);
                list.addAll(chatCardConfigs);
            }
            List<ChatCardConfig> chatCardConfigs = sysConfigService.getChatCardConfig(4, 1);
            list.addAll(chatCardConfigs);
        }

        list.sort(Comparator.comparingInt(ChatCardConfig::getSort));
        return list;
    }

    @Override
    public ConsultantVO getAiConsultant(Integer evaluationId, Integer userId) {
        Integer evaluationUserId = null;
        if (evaluationId != null) {
            EvaluationEntity evaluationEntity = evaluationService.getById(evaluationId);
            if (evaluationEntity != null && evaluationEntity.getHomeUserId() != null) {
                if (!userId.equals(evaluationEntity.getHomeUserId())) {
                    evaluationUserId = evaluationEntity.getHomeUserId();
                }
            }
        }
        ConsultantVO vo = null;
        if (SocketCache.socketUser.size() > 1) {
            Set<Integer> userIds = SocketCache.socketUser.keySet();
            vo = consultantService.getConsultantByUserIds(userIds, userId, evaluationUserId);
        }
        if (vo == null) {
            vo = consultantService.getConsultantByUserIds(null, userId, evaluationUserId);
        }
        return vo;
    }

}
