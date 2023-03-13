package com.home.modules.socket;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.home.common.dto.MessageDTO;
import com.home.common.utils.JsonResult;
import com.home.common.utils.JwtUtil;
import com.home.common.utils.PageBean;
import com.home.common.utils.SocketCache;
import com.home.common.vo.MessageVO;
import com.home.modules.generator.service.ChatRecordService;
import com.home.modules.sys.entity.SysUserEntity;
import com.home.modules.sys.service.SysUserTokenService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * @author xiewei
 * @date 2020/4/30 17:19
 */
@Slf4j
@Component
public class SocketHandler {

    @Resource
    private ChatRecordService chatRecordService;
    @Resource
    private SysUserTokenService tokenService;

    /**
     * sendMsg发送消息事件
     */
    @OnEvent(value = SocketCache.SEND_MSG_PATH)
    public void sendMsg(SocketIOClient client, AckRequest request, MessageDTO dto) {
        if (dto == null) {
            request.sendAckData(JsonResult.fail("数据不能为空"));
            return;
        }
        if (dto.getReceiveId() == null || dto.getSendId() == null || StringUtils.isEmpty(dto.getContent()) || dto.getType() == null) {
            request.sendAckData(JsonResult.fail("数据不能为空"));
            return;
        }
        Integer userId = getUserId(client);
        dto.setSendId(userId);
        JsonResult<MessageVO> jsonResult = chatRecordService.sendRecord(dto);
        UUID uuid = SocketCache.socketUser.get(dto.getReceiveId());
        if (uuid != null) {
            SocketIOClient socketClient = SocketCache.socketClient.get(uuid);
            if (socketClient != null) {
                socketClient.sendEvent(SocketCache.SEND_MSG_PATH, jsonResult);
            }
        }
        request.sendAckData(jsonResult);
    }

    /**
     * 获取历史消息
     */
    @OnEvent(value = SocketCache.HISTORICAL_NEWS_PATH)
    public void historicalNews(SocketIOClient client, AckRequest request, MessageDTO dto) {
        if (dto == null) {
            request.sendAckData(JsonResult.fail("数据不能为空"));
            return;
        }
        if (dto.getConsultantUserId() == null || dto.getPage() == null || dto.getSize() == null) {
            request.sendAckData(JsonResult.fail("数据不能为空"));
            return;
        }
        Integer userId = getUserId(client);
        JsonResult<PageBean<MessageVO>> historyRecord = chatRecordService.getHistoryRecord(dto, userId);
        request.sendAckData(historyRecord);
    }

    /**
     * 修改聊天记录状态
     */
    @OnEvent(value = SocketCache.UPDATE_MSG_STATUS_PATH)
    public void updateMsgStatus(SocketIOClient client, AckRequest request, MessageDTO dto) {
        if (dto == null) {
            request.sendAckData(JsonResult.fail("数据不能为空"));
            return;
        }
        if (dto.getConsultantUserId() == null) {
            request.sendAckData(JsonResult.fail("数据不能为空"));
            return;
        }
        Integer userId = getUserId(client);
        JsonResult jsonResult = chatRecordService.updateStatus(dto.getConsultantUserId(), userId);
        request.sendAckData(jsonResult);
    }

    /**
     * 获取userId
     */
    public Integer getUserId(SocketIOClient client) {
        HandshakeData data = client.getHandshakeData();
        String token = data.getSingleUrlParam("token");
        Integer userId;
        if (token != null && token.length() == 32) {
            SysUserEntity sysUserEntity = tokenService.getByToken(token);
            userId = sysUserEntity.getAppUserId();
        } else {
            userId = JwtUtil.getUserId(token);
        }
        return userId;
    }
}
