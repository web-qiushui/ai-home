package com.home.modules.generator.dao;

import com.home.modules.generator.entity.ConsultantAuditEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 顾问审核记录表
 * 
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-21 23:41:37
 */
@Mapper
public interface ConsultantAuditDao extends BaseMapper<ConsultantAuditEntity> {
	
}
