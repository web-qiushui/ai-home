package com.home.modules.generator.dao;

import com.home.common.dto.MessageDTO;
import com.home.common.vo.MessageVO;
import com.home.modules.generator.entity.ChatRecordEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 聊天记录表
 * 
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-25 10:00:21
 */
@Mapper
public interface ChatRecordDao extends BaseMapper<ChatRecordEntity> {

    /**
     * 修改聊天记录状态
     */
    Integer updateStatus(Integer consultantUserId, Integer userId);

    /**
     * 查询聊天记录
     */
    List<MessageVO> getHistoryRecord(Integer consultantUserId, Integer userId);

    /**
     * 根据类型和UUID获取聊天记录
     */
    ChatRecordEntity getChatRecordByUuidAndType(@Param("uuid") String uuid, @Param("type") Integer type);
	
}
