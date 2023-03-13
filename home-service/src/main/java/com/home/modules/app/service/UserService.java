package com.home.modules.app.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.home.common.dto.UserDTO;
import com.home.common.dto.WxUserDTO;
import com.home.common.utils.JsonResult;
import com.home.common.utils.PageBean;
import com.home.common.vo.BeanVO;
import com.home.common.vo.UserVO;
import com.home.modules.app.entity.UserEntity;
import com.home.modules.app.form.LoginForm;

import java.util.List;

/**
 * 用户
 *
 * @author xw
 */
public interface UserService extends IService<UserEntity> {

	UserEntity queryByMobile(String mobile);

	/**
	 * 用户登录
	 * @param form    登录表单
	 * @return        返回用户ID
	 */
	Integer login(LoginForm form);

	/**
	 * 微信登录
	 * @param dto    登录表单
	 * @return        返回用户ID
	 */
	Integer wxLogin(WxUserDTO dto);

	/**
	 * 根据用户Id获取用户基本信息
	 */
	UserVO userInfo(Integer userId);

	/**
	 * 修改用户信息
	 */
	JsonResult updateUserInfo(UserDTO dto);

	/**
	 * 查询用户列表
	 */
	PageBean<UserVO> getUserList(UserDTO dto);

	/**
	 * 查询非顾问列表
	 */
	List<BeanVO> getUserBeanList();
}
