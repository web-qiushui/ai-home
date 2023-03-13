package com.home.modules.job.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.home.common.utils.PageUtils;
import com.home.common.utils.Query;
import com.home.modules.job.dao.SysJobLogDao;
import com.home.modules.job.entity.SysJobLogEntity;
import com.home.modules.job.service.SysJobLogService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("sysJobLogService")
public class SysJobLogServiceImpl extends ServiceImpl<SysJobLogDao, SysJobLogEntity> implements SysJobLogService {

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		String jobId = (String)params.get("jobId");

		IPage<SysJobLogEntity> page = this.page(
			new Query<SysJobLogEntity>().getPage(params),
			new QueryWrapper<SysJobLogEntity>().like(StringUtils.isNotBlank(jobId),"job_id", jobId)
		);

		return new PageUtils(page);
	}

}
