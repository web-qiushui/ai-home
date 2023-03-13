package com.home.modules.generator.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.home.common.dto.MessageDTO;
import com.home.common.exception.RRException;
import com.home.common.utils.*;
import com.home.common.vo.DatadictVO;
import com.home.common.vo.UserVO;
import com.home.modules.generator.dao.ConsultantUserDao;
import com.home.modules.generator.entity.ConsultantEntity;
import com.home.modules.generator.entity.ConsultantUserEntity;
import com.home.modules.generator.service.ChatRecordService;
import com.home.modules.generator.service.ConsultantService;
import com.home.modules.generator.service.ConsultantUserService;
import com.home.modules.generator.vo.BuddyListVO;
import com.home.modules.generator.vo.ConsultantUserVO;
import com.home.modules.sys.service.SysDatadictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service("userConsultantService")
public class UserConsultantServiceImpl extends ServiceImpl<ConsultantUserDao, ConsultantUserEntity> implements ConsultantUserService {

    @Resource
    private ConsultantService consultantService;
    @Resource
    private ChatRecordService chatRecordService;
    @Resource
    private SysDatadictService sysDatadictService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ConsultantUserEntity> page = this.page(
                new Query<ConsultantUserEntity>().getPage(params),
                new QueryWrapper<ConsultantUserEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public Integer saveBuddy(Integer userId, Integer consultantId, Integer type, Integer recommendId) {
        ConsultantEntity consultantEntity = consultantService.getById(consultantId);
        if (consultantEntity == null) {
            throw new RRException("顾问为空");
        }
        if (!Constant.ConsultantStatus.SUCCESS.equals(consultantEntity.getStatus())) {
            throw new RRException("顾问未认证");
        }

        if (type != null && !type.equals(consultantEntity.getType())) {
            throw new RRException("顾问类型错误");
        }
        LambdaQueryWrapper<ConsultantUserEntity> queryWrapper = Wrappers.<ConsultantUserEntity>lambdaQuery()
                .eq(ConsultantUserEntity::getHomeUserId, userId)
                .eq(ConsultantUserEntity::getHomeConsultantId, consultantId);
        ConsultantUserEntity consultantUserEntity = baseMapper.selectOne(queryWrapper);
        if (consultantUserEntity == null) {
            consultantUserEntity = ConsultantUserEntity.builder()
                    .homeConsultantId(consultantId)
                    .homeUserId(userId)
                    .homeConsultantUserId(consultantEntity.getHomeUserId())
                    .status(Constant.Status.ENABLE.getType())
                    .recommendId(recommendId)
                    .build();
            if (type != null) {
                baseMapper.insert(consultantUserEntity);
                log.info("好友新增成功！关系Id：{}, type：{}", consultantUserEntity.getId(), type);
            }
        }
        if (Constant.AppRole.AI.equals(type)) {
            consultantEntity.setAdvisoryCount(consultantEntity.getAdvisoryCount() + 1);
        } else {
            consultantEntity.setBuyHomeCount(consultantEntity.getBuyHomeCount() + 1);
        }
        consultantService.updateById(consultantEntity);
        return consultantUserEntity.getId();
    }

    @Override
    public List<BuddyListVO> getBuddyList(Integer userId, String search) {
        List<ConsultantUserVO> consultantUsers = baseMapper.getBuddyList(userId);
        List<BuddyListVO> buddyList = new ArrayList<>();
        consultantUsers.forEach(consultantUserVO -> {
            BuddyListVO buddyListVO = new BuddyListVO();
            BeanUtils.copyProperties(consultantUserVO, buddyListVO);
            if (userId.equals(consultantUserVO.getUserId())) {
                buddyListVO.setSendId(consultantUserVO.getUserId());
                buddyListVO.setSendName(consultantUserVO.getNickName());
                buddyListVO.setSendAvatar(consultantUserVO.getAvatarUrl());
                buddyListVO.setReceiveId(consultantUserVO.getConsultantUserId());
                buddyListVO.setReceiveName(consultantUserVO.getName());
                buddyListVO.setReceiveAvatar(consultantUserVO.getAvatar());
            } else {
                buddyListVO.setSendId(consultantUserVO.getConsultantUserId());
                buddyListVO.setSendName(consultantUserVO.getName());
                buddyListVO.setSendAvatar(consultantUserVO.getAvatar());
                buddyListVO.setReceiveId(consultantUserVO.getUserId());
                buddyListVO.setReceiveName(consultantUserVO.getNickName());
                buddyListVO.setReceiveAvatar(consultantUserVO.getAvatarUrl());
            }
            if (buddyListVO.getUnread() == null) {
                buddyListVO.setUnread(0);
            }
            buddyList.add(buddyListVO);
        });
        return buddyList;
    }

    @Override
    public PageBean<ConsultantUserVO> getConsultantUserList(PageReq req) {
        PageHelper.startPage(req.getPage(), req.getSize());
        List<ConsultantUserVO> consultantUsers = baseMapper.getConsultantUserList();
        PageBean<ConsultantUserVO> pageBean = new PageBean<>(consultantUsers);
        return pageBean;
    }

    @Override
    public BuddyListVO getBuddyInfo(Integer id) {
        ConsultantUserVO consultantUserVO = baseMapper.getBuddyInfo(id);
        BuddyListVO buddyListVO = new BuddyListVO();
        BeanUtils.copyProperties(consultantUserVO, buddyListVO);
        buddyListVO.setSendId(consultantUserVO.getUserId());
        buddyListVO.setSendName(consultantUserVO.getNickName());
        buddyListVO.setSendAvatar(consultantUserVO.getAvatarUrl());
        buddyListVO.setReceiveId(consultantUserVO.getConsultantUserId());
        buddyListVO.setReceiveName(consultantUserVO.getName());
        buddyListVO.setReceiveAvatar(consultantUserVO.getAvatar());
        if (Constant.AppRole.AI.equals(consultantUserVO.getType())) {
            DatadictVO datadict = sysDatadictService.getDatadict(Constant.Datadict.AUTO_RESPONSE);
            String value = datadict.getValue();
            MessageDTO dto = new MessageDTO();
            dto.setSendId(buddyListVO.getReceiveId());
            dto.setReceiveId(buddyListVO.getSendId());
            dto.setConsultantUserId(buddyListVO.getId());
            dto.setType(1);
            value = value.replace("$name$", buddyListVO.getReceiveName());
            dto.setContent(value);
            chatRecordService.sendRecord(dto);
        }
        return buddyListVO;
    }

}