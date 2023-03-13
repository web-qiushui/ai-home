package com.home.modules.job.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.home.modules.job.entity.SysJobEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * 定时任务
 *
 * @author xw
 */
@Mapper
public interface SysJobDao extends BaseMapper<SysJobEntity> {
	
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
}
