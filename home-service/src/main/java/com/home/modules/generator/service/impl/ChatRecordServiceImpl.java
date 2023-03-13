package com.home.modules.generator.service.impl;

import com.github.pagehelper.PageHelper;
import com.home.common.dto.MessageDTO;
import com.home.common.utils.*;
import com.home.common.vo.MessageVO;
import com.home.modules.generator.entity.ConsultantUserEntity;
import com.home.modules.generator.service.ConsultantUserService;
import com.home.modules.generator.vo.EvaluationCommentVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.home.modules.generator.dao.ChatRecordDao;
import com.home.modules.generator.entity.ChatRecordEntity;
import com.home.modules.generator.service.ChatRecordService;

import javax.annotation.Resource;

@Slf4j
@Service("chatRecordService")
public class ChatRecordServiceImpl extends ServiceImpl<ChatRecordDao, ChatRecordEntity> implements ChatRecordService {

    @Resource
    private ConsultantUserService consultantUserService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ChatRecordEntity> page = this.page(
                new Query<ChatRecordEntity>().getPage(params),
                new QueryWrapper<ChatRecordEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public JsonResult<MessageVO> sendRecord(MessageDTO dto) {
        ConsultantUserEntity consultantUserEntity = consultantUserService.getById(dto.getConsultantUserId());
        if (consultantUserEntity == null) {
            return JsonResult.fail("用户顾问数据为空");
        }
        Integer userId = consultantUserEntity.getHomeUserId();
        Integer consultantUserId = consultantUserEntity.getHomeConsultantUserId();
        if (!(dto.getSendId().equals(userId) || dto.getSendId().equals(consultantUserId))) {
            return JsonResult.fail("用户无好友关系");
        }
        if (!(dto.getReceiveId().equals(userId) || dto.getReceiveId().equals(consultantUserId))) {
            return JsonResult.fail("用户无好友关系");
        }
        MessageVO vo = new MessageVO();
        ChatRecordEntity chatRecordEntity = ChatRecordEntity.builder()
                .content(dto.getContent())
                .homeConsultantUserId(dto.getConsultantUserId())
                .sendId(dto.getSendId())
                .receiveId(dto.getReceiveId())
                .type(dto.getType())
                .status(Constant.Status.DISABLE.getType())
                .build();
        baseMapper.insert(chatRecordEntity);
        BeanUtils.copyProperties(dto, vo);
        vo.setCreateTime(chatRecordEntity.getCreateTime());
        return JsonResult.success(vo);
    }

    @Override
    public JsonResult<PageBean<MessageVO>> getHistoryRecord(MessageDTO dto, Integer userId) {
        ConsultantUserEntity consultantUserEntity = consultantUserService.getById(dto.getConsultantUserId());
        if (consultantUserEntity == null) {
            return JsonResult.fail("用户顾问数据为空");
        }
        if (!(userId.equals(consultantUserEntity.getHomeUserId()) || userId.equals(consultantUserEntity.getHomeConsultantUserId()))) {
            return JsonResult.fail("用户顾问数据为空");
        }
        PageHelper.startPage(dto.getPage(), dto.getSize());
        List<MessageVO> list = baseMapper.getHistoryRecord(dto.getConsultantUserId(), userId);
        list.sort(Comparator.comparingInt(MessageVO::getId));
        PageBean<MessageVO> pageBean = new PageBean<>(list);
        return JsonResult.success(pageBean);
    }

    @Override
    public JsonResult updateStatus(Integer consultantUserId, Integer userId) {
        // 更新消息是否读取
        baseMapper.updateStatus(consultantUserId, userId);
        return JsonResult.success();
    }

    @Override
    public JsonResult<PageBean<MessageVO>> getHistoryRecord(MessageDTO dto) {
        ConsultantUserEntity consultantUserEntity = consultantUserService.getById(dto.getConsultantUserId());
        if (consultantUserEntity == null) {
            return JsonResult.fail("用户顾问数据为空");
        }
        PageHelper.startPage(dto.getPage(), dto.getSize());
        List<MessageVO> list = baseMapper.getHistoryRecord(dto.getConsultantUserId(), null);
        list.sort(Comparator.comparingInt(MessageVO::getId));
        PageBean<MessageVO> pageBean = new PageBean<>(list);
        return JsonResult.success(pageBean);
    }

    @Override
    public ChatRecordEntity getChatRecordByUuidAndType(String uuid, Integer type) {
        return baseMapper.getChatRecordByUuidAndType(uuid, type);
    }

}