package com.home.modules.generator.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.home.modules.generator.dto.ConsultantDTO;
import com.home.modules.generator.dto.ConsultantUserDTO;
import com.home.modules.generator.entity.ConsultantEntity;
import com.home.modules.generator.vo.ConsultantVO;
import com.home.modules.generator.vo.FirmCountVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 顾问表
 *
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-11 21:12:12
 */
@Mapper
public interface ConsultantDao extends BaseMapper<ConsultantEntity> {

    /**
     * 查询企业服务人数列表
     */
    List<FirmCountVO> getFirmServerCountList(ConsultantUserDTO dto);

    /**
     * 查询置业顾问列表
     */
    List<ConsultantVO> getHomeConsultantList(ConsultantUserDTO dto);

    /**
     * 查询顾问列表
     */
    List<ConsultantVO> getConsultantList(ConsultantDTO dto);

    /**
     * 查询用户顾问列表
     */
    List<ConsultantVO> getConsultantUserList(Integer userId, Integer type);

    /**
     * 根据用户Id列表随机筛选1个AI顾问
     */
    ConsultantVO getConsultantByUserIds(@Param("userIds") Set<Integer> userIds,
                                        @Param("userId") Integer userId,
                                        @Param("evaluationUserId") Integer evaluationUserId);
}
