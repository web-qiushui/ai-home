package com.home.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.home.modules.sys.entity.SysCaptchaEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 验证码
 *
 * @author xw
 */
@Mapper
public interface SysCaptchaDao extends BaseMapper<SysCaptchaEntity> {

}
