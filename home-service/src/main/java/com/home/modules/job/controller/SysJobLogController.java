package com.home.modules.job.controller;

import com.home.common.utils.PageUtils;
import com.home.common.utils.R;
import com.home.modules.job.entity.SysJobLogEntity;
import com.home.modules.job.service.SysJobLogService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 定时任务日志
 *
 * @author xw
 */
@RestController
@RequestMapping("/sys/jobLog")
public class SysJobLogController {

	@Resource
	private SysJobLogService sysJobLogService;
	
	/**
	 * 定时任务日志列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("sys:job:log")
	public R list(@RequestParam Map<String, Object> params){
		PageUtils page = sysJobLogService.queryPage(params);
		
		return R.ok().put("page", page);
	}
	
	/**
	 * 定时任务日志信息
	 */
	@RequestMapping("/info/{logId}")
	public R info(@PathVariable("logId") Long logId){
		SysJobLogEntity log = sysJobLogService.getById(logId);
		
		return R.ok().put("log", log);
	}
}
