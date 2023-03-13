package com.home.modules.generator.dao;

import com.home.modules.generator.entity.LoginEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户登录记录表
 * 
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-07-22 22:25:48
 */
@Mapper
public interface LoginDao extends BaseMapper<LoginEntity> {

    /**
     * 查询在线人数
     * @param type day/month/year 日、月、年
     * @return
     */
    Integer getLoginStatistics(String type);
	
}
