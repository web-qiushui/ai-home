package com.home.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.home.common.utils.R;
import com.home.modules.sys.entity.SysUserEntity;
import com.home.modules.sys.entity.SysUserTokenEntity;

/**
 * 用户Token
 *
 * @author xw
 */
public interface SysUserTokenService extends IService<SysUserTokenEntity> {

	/**
	 * 生成token
	 * @param userId  用户ID
	 */
	R createToken(long userId);

	/**
	 * 退出，修改token值
	 * @param userId  用户ID
	 */
	void logout(long userId);

	SysUserTokenEntity queryByToken(String token);

	SysUserEntity getByToken(String token);

}
