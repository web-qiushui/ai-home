package com.home.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.home.common.constant.config.*;
import com.home.common.utils.JsonResult;
import com.home.common.utils.PageUtils;
import com.home.modules.sys.entity.SysConfigEntity;

import java.util.List;
import java.util.Map;

/**
 * 系统配置信息
 *
 * @author xw
 */
public interface SysConfigService extends IService<SysConfigEntity> {

	PageUtils queryPage(Map<String, Object> params);
	
	/**
	 * 保存配置信息
	 */
	public void saveConfig(SysConfigEntity config);
	
	/**
	 * 更新配置信息
	 */
	public void update(SysConfigEntity config);
	
	/**
	 * 根据key，更新value
	 */
	public void updateValueByKey(String key, String value);
	
	/**
	 * 删除配置信息
	 */
	public void deleteBatch(Long[] ids);
	
	/**
	 * 根据key，获取配置的value值
	 * 
	 * @param key           key
	 */
	public String getValue(String key);
	
	/**
	 * 根据key，获取value的Object对象
	 * @param key    key
	 * @param clazz  Object对象
	 */
	public <T> T getConfigObject(String key, Class<T> clazz);

	/**
	 * 修改或新增金刚区
	 */
	JsonResult saveKingKongConfig(KingKongConfig kingKongConfig);

	/**
	 * 修改或新增新人指引
	 */
	JsonResult saveNewcomerGuideConfig(NewcomerGuideConfig newcomerGuideConfig);

	/**
	 * 修改或新增开关配置
	 */
	JsonResult saveSwitchConfig(SwitchConfig switchConfig);

	/**
	 * 修改或新增微信配置
	 */
	JsonResult saveWxConfig(WxConfig wxConfig);

	/**
	 * 修改或新增阿里云短信
	 */
	JsonResult saveAliyunSmsConfig(AliyunSmsConfig aliyunSmsConfig);

	/**
	 * 修改或新增购房需求配置
	 */
	JsonResult saveReleaseConfig(ReleaseConfig releaseConfig);

	/**
	 * 修改或新增网站配置
	 */
	JsonResult saveWebsiteConfig(WebsiteConfig websiteConfig);

	/**
	 * 修改或新增快速回复配置
	 */
	JsonResult saveQuickReplyConfig(QuickReplyConfig quickReplyConfig);

	/**
	 * 修改或新增聊天卡片配置
	 */
	JsonResult saveChatCardConfig(ChatCardConfig chatCardConfig);

	/**
	 * 查询聊天卡片配置
	 */
	List<ChatCardConfig> getChatCardConfig(Integer auth, Integer status);

	/**
	 * 查询快速回复配置
	 */
	List<QuickReplyConfig> getQuickReplyConfig(Integer type, Integer status);

	/**
	 * 查询网站配置
	 */
	WebsiteConfig getWebsiteConfig();

	/**
	 * 查询金刚区内容
	 */
	List<KingKongConfig> getKingKongConfig(Integer status);

	/**
	 * 查询新人指引
	 */
	List<NewcomerGuideConfig> getNewcomerGuideConfig(Integer status);

	/**
	 * 查询开关配置
	 */
	List<SwitchConfig> getSwitchConfig(Boolean value);

	/**
	 * 查询微信配置
	 */
	WxConfig getWxConfig();

	/**
	 * 查询阿里云短信配置
	 */
	AliyunSmsConfig getAliyunSmsConfig();

	/**
	 * 查询购房需求配置
	 */
	ReleaseConfig getReleaseConfig();

	/**
	 * 根据类型查询金刚区内容
	 */
	KingKongConfig getKingKongConfig(String type);

	/**
	 * 根据类型查询新人指引
	 */
	NewcomerGuideConfig getNewcomerGuideConfig(String type);

	/**
	 * 根据类型查询开关配置
	 */
	SwitchConfig getSwitchConfig(String type);

	/**
	 * 删除新人指引
	 */
	JsonResult removeNewcomerGuideConfig(String type);

	/**
	 * 删除开关配置
	 */
	JsonResult removeSwitchConfig(String key);

	/**
	 * 删除快速回复配置
	 */
	JsonResult removeQuickReplyConfig(String id);

	/**
	 * 排序新人指引
	 */
	JsonResult sortNewcomerGuideConfig(List<String> types);

	/**
	 * 排序快速回复配置
	 */
	JsonResult sortQuickReplyConfig(List<String> ids);



}
