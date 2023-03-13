package com.home.modules.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.home.common.utils.R;
import com.home.modules.sys.dao.SysUserTokenDao;
import com.home.modules.sys.entity.SysUserEntity;
import com.home.modules.sys.entity.SysUserTokenEntity;
import com.home.modules.sys.oauth2.TokenGenerator;
import com.home.modules.sys.service.SysUserService;
import com.home.modules.sys.service.SysUserTokenService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;


@Service("sysUserTokenService")
public class SysUserTokenServiceImpl extends ServiceImpl<SysUserTokenDao, SysUserTokenEntity> implements SysUserTokenService {
	//7天后过期
	private final static int EXPIRE = 3600 * 24 * 7;

	@Resource
	private SysUserService sysUserService;


	@Override
	public R createToken(long userId) {
		//生成一个token
		String token = TokenGenerator.generateValue();

		//当前时间
		Date now = new Date();
		//过期时间
		Date expireTime = new Date(now.getTime() + EXPIRE * 1000);

		//判断是否生成过token
		SysUserTokenEntity tokenEntity = this.getById(userId);
		if(tokenEntity == null){
			tokenEntity = new SysUserTokenEntity();
			tokenEntity.setUserId(userId);
			tokenEntity.setToken(token);
			tokenEntity.setUpdateTime(now);
			tokenEntity.setExpireTime(expireTime);

			//保存token
			this.save(tokenEntity);
		}else{
			tokenEntity.setToken(token);
			tokenEntity.setUpdateTime(now);
			tokenEntity.setExpireTime(expireTime);

			//更新token
			this.updateById(tokenEntity);
		}

		R r = R.ok().put("token", token).put("expire", EXPIRE);

		return r;
	}

	@Override
	public void logout(long userId) {
		//生成一个token
		String token = TokenGenerator.generateValue();

		//修改token
		SysUserTokenEntity tokenEntity = new SysUserTokenEntity();
		tokenEntity.setUserId(userId);
		tokenEntity.setToken(token);
		this.updateById(tokenEntity);
	}

	@Override
	public SysUserTokenEntity queryByToken(String token) {
		return baseMapper.queryByToken(token);
	}

	@Override
	public SysUserEntity getByToken(String token) {
		SysUserTokenEntity sysUserTokenEntity = baseMapper.queryByToken(token);
		if (sysUserTokenEntity == null) {
			return null;
		}
		return sysUserService.getById(sysUserTokenEntity.getUserId());
	}
}
