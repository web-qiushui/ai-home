package com.home.modules.job.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.home.modules.job.entity.SysJobLogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 定时任务日志
 *
 * @author xw
 */
@Mapper
public interface SysJobLogDao extends BaseMapper<SysJobLogEntity> {
	
}
