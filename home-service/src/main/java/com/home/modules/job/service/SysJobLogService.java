package com.home.modules.job.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.home.common.utils.PageUtils;
import com.home.modules.job.entity.SysJobLogEntity;

import java.util.Map;

/**
 * 定时任务日志
 *
 * @author xw
 */
public interface SysJobLogService extends IService<SysJobLogEntity> {

	PageUtils queryPage(Map<String, Object> params);
	
}
