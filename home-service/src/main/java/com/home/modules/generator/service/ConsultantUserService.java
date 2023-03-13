package com.home.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.home.common.utils.JsonResult;
import com.home.common.utils.PageBean;
import com.home.common.utils.PageReq;
import com.home.common.utils.PageUtils;
import com.home.modules.generator.entity.ConsultantUserEntity;
import com.home.modules.generator.vo.BuddyListVO;
import com.home.modules.generator.vo.ConsultantUserVO;

import java.util.List;
import java.util.Map;

/**
 * 用户顾问表
 *
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-18 00:06:25
 */
public interface ConsultantUserService extends IService<ConsultantUserEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存好友
     *
     */
    Integer saveBuddy(Integer userId, Integer consultantId, Integer type, Integer recommendId);

    /**
     * 获取好友列表
     */
    List<BuddyListVO> getBuddyList(Integer userId, String search);

    /**
     * 获取所有用户顾问列表
     * @return
     */
    PageBean<ConsultantUserVO> getConsultantUserList(PageReq req);

    /**
     * 根据Id获取好友信息
     */
    BuddyListVO getBuddyInfo(Integer id);

}

