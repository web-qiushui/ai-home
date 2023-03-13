package com.home.modules.generator.dao;

import com.home.modules.generator.dto.DemandDTO;
import com.home.modules.generator.entity.DemandEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.home.modules.generator.vo.DemandVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 购房需求表
 *
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-11 21:12:12
 */
@Mapper
public interface DemandDao extends BaseMapper<DemandEntity> {

    /**
     * 根据用户id查询我的发布
     * @param userId
     * @return
     */
    List<DemandVO> getMyPublishByUserId(Integer userId);

    /**
     * 查询购房需求列表
     * @param dto
     * @return
     */
    List<DemandVO> getDemandList(DemandDTO dto);


}
