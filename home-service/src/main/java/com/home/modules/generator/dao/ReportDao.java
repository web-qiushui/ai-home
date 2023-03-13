package com.home.modules.generator.dao;

import com.home.modules.generator.dto.ReportDTO;
import com.home.modules.generator.entity.ReportEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.home.modules.generator.vo.ReportVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 购房报告表
 * 
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-07-25 13:53:42
 */
@Mapper
public interface ReportDao extends BaseMapper<ReportEntity> {

    /**
     * 查询列表
     * @param dto
     * @return
     */
    List<ReportVO> getHomeReportList(ReportDTO dto);
	
}
