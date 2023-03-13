package com.home.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.home.common.dto.SinglePageDTO;
import com.home.common.vo.SinglePageVO;
import com.home.modules.sys.entity.SysSinglePageEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 系统单页面表
 * 
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-28 22:14:45
 */
@Mapper
public interface SysSinglePageDao extends BaseMapper<SysSinglePageEntity> {

    /**
     * 查询单页面列表
     * @param dto
     * @return
     */
    List<SinglePageVO> getSinglePageList(SinglePageDTO dto);
	
}
