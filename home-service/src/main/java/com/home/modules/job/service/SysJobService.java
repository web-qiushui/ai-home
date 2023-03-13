package com.home.modules.job.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.home.common.utils.PageUtils;
import com.home.modules.job.entity.SysJobEntity;

import java.util.Map;

/**
 * 定时任务
 *
 * @author xw
 */
public interface SysJobService extends IService<SysJobEntity> {

	PageUtils queryPage(Map<String, Object> params);

	/**
	 * 保存定时任务
	 */
	void saveJob(SysJobEntity scheduleJob);
	
	/**
	 * 更新定时任务
	 */
	void update(SysJobEntity scheduleJob);
	
	/**
	 * 批量删除定时任务
	 */
	void deleteBatch(Long[] jobIds);
	
	/**
	 * 批量更新定时任务状态
	 */
	int updateBatch(Long[] jobIds, int status);
	
	/**
	 * 立即执行
	 */
	void run(Long[] jobIds);
	
	/**
	 * 暂停运行
	 */
	void pause(Long[] jobIds);
	
	/**
	 * 恢复运行
	 */
	void resume(Long[] jobIds);
}
