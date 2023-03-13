package com.home.modules.job.controller;

import com.home.common.annotation.SysLog;
import com.home.common.utils.PageUtils;
import com.home.common.utils.R;
import com.home.common.validator.ValidatorUtils;
import com.home.modules.job.entity.SysJobEntity;
import com.home.modules.job.service.SysJobService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 定时任务
 *
 * @author xw
 */
@RestController
@RequestMapping("/sys/job")
public class SysJobController {

	@Resource
	private SysJobService sysJobService;
	
	/**
	 * 定时任务列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("sys:job:list")
	public R list(@RequestParam Map<String, Object> params){
		PageUtils page = sysJobService.queryPage(params);

		return R.ok().put("page", page);
	}
	
	/**
	 * 定时任务信息
	 */
	@RequestMapping("/info/{jobId}")
	@RequiresPermissions("sys:job:info")
	public R info(@PathVariable("jobId") Long jobId){
		SysJobEntity schedule = sysJobService.getById(jobId);
		
		return R.ok().put("schedule", schedule);
	}
	
	/**
	 * 保存定时任务
	 */
	@SysLog("保存定时任务")
	@RequestMapping("/save")
	@RequiresPermissions("sys:job:save")
	public R save(@RequestBody SysJobEntity scheduleJob){
		ValidatorUtils.validateEntity(scheduleJob);
		
		sysJobService.saveJob(scheduleJob);
		
		return R.ok();
	}
	
	/**
	 * 修改定时任务
	 */
	@SysLog("修改定时任务")
	@RequestMapping("/update")
	@RequiresPermissions("sys:job:update")
	public R update(@RequestBody SysJobEntity scheduleJob){
		ValidatorUtils.validateEntity(scheduleJob);
				
		sysJobService.update(scheduleJob);
		
		return R.ok();
	}
	
	/**
	 * 删除定时任务
	 */
	@SysLog("删除定时任务")
	@RequestMapping("/delete")
	@RequiresPermissions("sys:job:delete")
	public R delete(@RequestBody Long[] jobIds){
		sysJobService.deleteBatch(jobIds);
		
		return R.ok();
	}
	
	/**
	 * 立即执行任务
	 */
	@SysLog("立即执行任务")
	@RequestMapping("/run")
	@RequiresPermissions("sys:job:run")
	public R run(@RequestBody Long[] jobIds){
		sysJobService.run(jobIds);
		
		return R.ok();
	}
	
	/**
	 * 暂停定时任务
	 */
	@SysLog("暂停定时任务")
	@RequestMapping("/pause")
	@RequiresPermissions("sys:job:pause")
	public R pause(@RequestBody Long[] jobIds){
		sysJobService.pause(jobIds);
		
		return R.ok();
	}
	
	/**
	 * 恢复定时任务
	 */
	@SysLog("恢复定时任务")
	@RequestMapping("/resume")
	@RequiresPermissions("sys:job:resume")
	public R resume(@RequestBody Long[] jobIds){
		sysJobService.resume(jobIds);
		
		return R.ok();
	}

}
