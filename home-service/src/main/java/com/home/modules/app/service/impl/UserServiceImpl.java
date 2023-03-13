package com.home.modules.app.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.home.common.constant.ConfigConstant;
import com.home.common.constant.config.SwitchConfig;
import com.home.common.constant.config.WxConfig;
import com.home.common.dto.UserDTO;
import com.home.common.dto.WxUserDTO;
import com.home.common.exception.RRException;
import com.home.common.utils.*;
import com.home.common.validator.Assert;
import com.home.common.vo.BeanVO;
import com.home.common.vo.UserVO;
import com.home.modules.app.dao.UserDao;
import com.home.modules.app.entity.UserEntity;
import com.home.modules.app.form.LoginForm;
import com.home.modules.app.service.UserService;
import com.home.modules.generator.entity.ConsultantEntity;
import com.home.modules.generator.service.ConsultantService;
import com.home.modules.sys.service.SysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {

	@Resource
	private SysConfigService sysConfigService;
	@Resource
	private ConsultantService consultantService;

	@Override
	public UserEntity queryByMobile(String mobile) {
		return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("user_name", mobile));
	}

	@Override
	public Integer login(LoginForm form) {
		UserEntity user = queryByMobile(form.getMobile());
		Assert.isNull(user, "手机号或密码错误");

		//密码错误
		if(!user.getPassWord().equals(DigestUtils.sha256Hex(form.getPassword()))){
			throw new RRException("手机号或密码错误");
		}

		return user.getId();
	}

	@Override
    @Transactional(rollbackFor = Exception.class)
	public Integer wxLogin(WxUserDTO dto) {
		SwitchConfig switchConfig = sysConfigService.getSwitchConfig(ConfigConstant.WX_CONFIG);
		Assert.isNull(switchConfig, "微信登录未开启");
		if (!switchConfig.getValue()) {
			throw new RRException("微信登录未开启");
		}
		WxConfig wxConfig = sysConfigService.getWxConfig();
		Assert.isNull(wxConfig, "微信小程序未配置");
		if (StringUtils.isEmpty(wxConfig.getAppId()) || StringUtils.isEmpty(wxConfig.getAppSecret())) {
			throw new RRException("微信小程序未配置");
		}
		JSONObject jsonObject = WxUtils.code2Session(wxConfig.getAppId(), wxConfig.getAppSecret(), dto.getCode());
		Assert.isNull(jsonObject, "微信小程序登录失败");
		if (jsonObject.getInteger("errcode") != null && jsonObject.getInteger("errcode") != 0) {
			throw new RRException("微信小程序登录失败");
		}

		QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<UserEntity>().eq("open_id", jsonObject.getString("openid"));
		UserEntity userEntity = baseMapper.selectOne(queryWrapper);
		if (userEntity == null) {
			userEntity = new UserEntity();
			userEntity.setRole(Constant.AppRole.USER);
            userEntity.setStatus(Constant.Status.ENABLE.getType());
		}
		String openId = jsonObject.getString("openid");
		String unionId = jsonObject.getString("unionid");
		String sessionKey = jsonObject.getString("session_key");

		if (StringUtils.isNotEmpty(openId)) {
			userEntity.setOpenId(openId);
		}
		if (StringUtils.isNotEmpty(unionId)) {
			userEntity.setUnionId(unionId);
		}
		if (StringUtils.isNotEmpty(sessionKey)) {
			userEntity.setSessionKey(sessionKey);
		}

		if (WxUtils.signature(dto.getRawData(), sessionKey, dto.getSignature())) {
            WxUserDTO.UserInfo userInfo = JSON.parseObject(dto.getRawData(), WxUserDTO.UserInfo.class);
			if (StringUtils.isNotEmpty(userInfo.getNickName())) {
				userEntity.setNickName(userInfo.getNickName());
			}
			if (StringUtils.isNotEmpty(userInfo.getAvatarUrl())) {
				userEntity.setAvatarUrl(userInfo.getAvatarUrl());
			}
			if (userEntity.getGender() != null) {
				if (userInfo.getGender() != null) {
					userEntity.setGender(userInfo.getGender());
				}
			}
            StringBuilder address = new StringBuilder();
            if (StringUtils.isNotEmpty(userInfo.getCountry())) {
				address.append(userInfo.getCountry());
            }
            if (StringUtils.isNotEmpty(userInfo.getProvince())) {
				address.append(userInfo.getProvince());
            }
            if (StringUtils.isNotEmpty(userInfo.getCity())) {
				address.append(userInfo.getCity());
            }
			if (StringUtils.isNotEmpty(userEntity.getAddress())) {
				if (StringUtils.isNotEmpty(address.toString())) {
					userEntity.setAddress(address.toString());
				}
			}
        }

		this.saveOrUpdate(userEntity);
		log.info("微信登录成功==>userEntity：{}", userEntity);
		return userEntity.getId();
	}

	@Override
	public UserVO userInfo(Integer userId) {
		UserEntity userEntity = baseMapper.selectById(userId);
		UserVO vo = new UserVO();
		BeanUtils.copyProperties(userEntity, vo);
		LambdaQueryWrapper<ConsultantEntity> lambdaQueryWrapper = Wrappers.<ConsultantEntity>lambdaQuery()
				.eq(ConsultantEntity::getHomeUserId, userId);
		ConsultantEntity consultantEntity = consultantService.getOne(lambdaQueryWrapper);
		if (consultantEntity == null) {
			vo.setAuthenticateStatus(Constant.ConsultantStatus.NO);
		} else {
			vo.setAuthenticateStatus(consultantEntity.getStatus());
		}
		return vo;
	}

	@Override
	public JsonResult updateUserInfo(UserDTO dto) {
		UserEntity userEntity = baseMapper.selectById(dto.getId());
		if (userEntity == null) {
			return JsonResult.fail("用户为空");
		}

		if (StringUtils.isNotEmpty(dto.getPhone())) {
			if (!ValidatorUtils.isMobile(dto.getPhone())) {
				return JsonResult.fail("手机号码格式错误");
			}
			userEntity.setPhone(dto.getPhone());
		}
		if (StringUtils.isNotEmpty(dto.getIdCard())) {
			if (!ValidatorUtils.isIDCard(dto.getIdCard())) {
				return JsonResult.fail("身份证格式错误");
			}
			userEntity.setIdCard(dto.getIdCard());
		}
		if (StringUtils.isNotEmpty(dto.getAddress())) {
			userEntity.setAddress(dto.getAddress());
		}
		if (dto.getGender() != null) {
			userEntity.setGender(dto.getGender());
		}
		baseMapper.updateById(userEntity);
		return JsonResult.success();
	}

	@Override
	public PageBean<UserVO> getUserList(UserDTO dto) {
		PageHelper.startPage(dto.getPage(), dto.getSize());
		List<UserVO> userList = baseMapper.getUserList(dto);
		PageBean<UserVO> pageBean = new PageBean<>(userList);
		return pageBean;
	}

	@Override
	public List<BeanVO> getUserBeanList() {
		return baseMapper.getUserBeanList();
	}
}
