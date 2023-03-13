package com.home.modules.oss.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.home.modules.oss.entity.SysOssEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文件上传
 *
 * @author xw
 */
@Mapper
public interface SysOssDao extends BaseMapper<SysOssEntity> {
	
}
