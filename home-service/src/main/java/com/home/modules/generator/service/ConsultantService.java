package com.home.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.home.common.utils.PageBean;
import com.home.common.utils.PageUtils;
import com.home.modules.generator.dto.ConsultantDTO;
import com.home.modules.generator.dto.ConsultantUserDTO;
import com.home.modules.generator.entity.ConsultantEntity;
import com.home.modules.generator.vo.ConsultantVO;

import java.util.Map;
import java.util.Set;

/**
 * 顾问表
 *
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-11 21:12:12
 */
public interface ConsultantService extends IService<ConsultantEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 查询置业顾问列表
     */
    PageBean<ConsultantVO> getConsultantList(ConsultantUserDTO dto);

    /**
     * 查询顾问列表
     */
    PageBean<ConsultantVO> getConsultantList(ConsultantDTO dto);

    /**
     * 查询用户顾问列表
     */
    PageBean<ConsultantVO> getConsultantUserList(ConsultantUserDTO dto);

    /**
     * 根据顾问Id查询顾问详情
     */
    ConsultantVO getConsultantInfo(Integer id);

    /**
     * 根据用户Id查询顾问信息
     */
    ConsultantVO getConsultantByUserId(Integer userId);

    /**
     * 根据用户Id列表查询顾问信息
     */
    ConsultantVO getConsultantByUserIds(Set<Integer> userIds, Integer userId, Integer evaluationUserId);
}

