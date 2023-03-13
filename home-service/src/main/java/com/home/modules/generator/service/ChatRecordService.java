package com.home.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.home.common.dto.MessageDTO;
import com.home.common.utils.JsonResult;
import com.home.common.utils.PageBean;
import com.home.common.utils.PageUtils;
import com.home.common.vo.MessageVO;
import com.home.modules.generator.entity.ChatRecordEntity;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 聊天记录表
 *
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-25 10:00:21
 */
public interface ChatRecordService extends IService<ChatRecordEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 聊天
     */
    JsonResult<MessageVO> sendRecord(MessageDTO dto);

    /**
     * 查询聊天记录
     */
    JsonResult<PageBean<MessageVO>> getHistoryRecord(MessageDTO dto, Integer userId);

    /**
     * 修改聊天记录状态
     */
    JsonResult updateStatus(Integer consultantUserId, Integer userId);

    /**
     * 查询聊天记录
     */
    JsonResult<PageBean<MessageVO>> getHistoryRecord(MessageDTO dto);

    /**
     * 根据类型和UUID获取聊天记录
     */
    ChatRecordEntity getChatRecordByUuidAndType(String uuid, Integer type);

}

