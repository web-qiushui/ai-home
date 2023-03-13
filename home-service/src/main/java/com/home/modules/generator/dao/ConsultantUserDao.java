package com.home.modules.generator.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.home.modules.generator.entity.ConsultantUserEntity;
import com.home.modules.generator.vo.ConsultantUserVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户顾问表
 * 
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-18 00:06:25
 */
@Mapper
public interface ConsultantUserDao extends BaseMapper<ConsultantUserEntity> {

    /**
     * 根据用户顾问表Id好友信息
     */
    ConsultantUserVO getBuddyInfo(Integer id);

    /**
     * 根据用户Id查询好友列表
     * @param userId 用户Id
     */
    List<ConsultantUserVO> getBuddyList(Integer userId);

    /**
     * 获取用户顾问列表
     */
    List<ConsultantUserVO> getConsultantUserList();

}
