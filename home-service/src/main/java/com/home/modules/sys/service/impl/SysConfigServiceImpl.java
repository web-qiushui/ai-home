package com.home.modules.sys.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.home.common.constant.ConfigConstant;
import com.home.common.constant.config.*;
import com.home.common.exception.RRException;
import com.home.common.utils.JsonResult;
import com.home.common.utils.PageUtils;
import com.home.common.utils.Query;
import com.home.modules.generator.service.DemandService;
import com.home.modules.sys.dao.SysConfigDao;
import com.home.modules.sys.entity.SysConfigEntity;
import com.home.modules.sys.redis.SysConfigRedis;
import com.home.modules.sys.service.SysConfigService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("sysConfigService")
public class SysConfigServiceImpl extends ServiceImpl<SysConfigDao, SysConfigEntity> implements SysConfigService {
	@Resource
	private SysConfigRedis sysConfigRedis;
	@Resource
	private DemandService demandService;

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		String paramKey = (String)params.get("paramKey");

		IPage<SysConfigEntity> page = this.page(
			new Query<SysConfigEntity>().getPage(params),
			new QueryWrapper<SysConfigEntity>()
				.like(StringUtils.isNotBlank(paramKey),"param_key", paramKey));
		return new PageUtils(page);
	}

	@Override
	public void saveConfig(SysConfigEntity config) {
		this.save(config);
		sysConfigRedis.saveOrUpdate(config);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(SysConfigEntity config) {
		this.updateById(config);
		sysConfigRedis.saveOrUpdate(config);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateValueByKey(String key, String value) {
		baseMapper.updateValueByKey(key, value);
		sysConfigRedis.delete(key);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteBatch(Long[] ids) {
		for(Long id : ids){
			SysConfigEntity config = this.getById(id);
			sysConfigRedis.delete(config.getParamKey());
		}

		this.removeByIds(Arrays.asList(ids));
	}

	@Override
	public String getValue(String key) {
		SysConfigEntity config = sysConfigRedis.get(key);
		if(config == null){
			config = baseMapper.queryByKey(key);
			sysConfigRedis.saveOrUpdate(config);
		}

		return config == null ? null : config.getParamValue();
	}
	
	@Override
	public <T> T getConfigObject(String key, Class<T> clazz) {
		String value = getValue(key);
		if(StringUtils.isNotBlank(value)){
			return new Gson().fromJson(value, clazz);
		}

		try {
			return clazz.newInstance();
		} catch (Exception e) {
			throw new RRException("获取参数失败");
		}
	}

	@Override
	public JsonResult saveKingKongConfig(KingKongConfig kingKongConfig) {
		SysConfigEntity sysConfigEntity = baseMapper.queryByKey(ConfigConstant.KING_KONG_CONFIG);
		List<KingKongConfig> kingKongConfigs = JSON.parseArray(sysConfigEntity.getParamValue(), KingKongConfig.class);
		Map<String, KingKongConfig> collect = kingKongConfigs.stream().collect(Collectors.toMap(KingKongConfig::getType, Function.identity()));
		if (collect.get(kingKongConfig.getType()) == null) {
			kingKongConfigs.add(kingKongConfig);
		} else {
			kingKongConfigs.forEach(config -> {
				if (config.getType().equals(kingKongConfig.getType())) {
					BeanUtils.copyProperties(kingKongConfig, config);
				}
			});
		}
		sysConfigEntity.setParamValue(JSON.toJSONString(kingKongConfigs));
		baseMapper.updateById(sysConfigEntity);
		return JsonResult.success();
	}

	@Override
	public JsonResult saveNewcomerGuideConfig(NewcomerGuideConfig newcomerGuideConfig) {
		SysConfigEntity sysConfigEntity = baseMapper.queryByKey(ConfigConstant.NEWCOMER_GUIDE_CONFIG);
		List<NewcomerGuideConfig> newcomerGuideConfigs = JSON.parseArray(sysConfigEntity.getParamValue(), NewcomerGuideConfig.class);
		Map<String, NewcomerGuideConfig> collect = newcomerGuideConfigs.stream().collect(Collectors.toMap(NewcomerGuideConfig::getType, Function.identity()));
		if (collect.get(newcomerGuideConfig.getType()) == null) {
			newcomerGuideConfigs.add(newcomerGuideConfig);
		} else {
			newcomerGuideConfigs.forEach(config -> {
				if (config.getType().equals(newcomerGuideConfig.getType())) {
					BeanUtils.copyProperties(newcomerGuideConfig, config);
				}
			});
		}
		sysConfigEntity.setParamValue(JSON.toJSONString(newcomerGuideConfigs));
		baseMapper.updateById(sysConfigEntity);
		return JsonResult.success();
	}

	@Override
	public JsonResult saveSwitchConfig(SwitchConfig switchConfig) {
		SysConfigEntity sysConfigEntity = baseMapper.queryByKey(ConfigConstant.SWITCH_CONFIG);
		if (sysConfigEntity == null) {
			return JsonResult.fail("配置为空");
		}
		List<SwitchConfig> switchConfigs = JSON.parseArray(sysConfigEntity.getParamValue(), SwitchConfig.class);
		Map<String, SwitchConfig> collect = switchConfigs.stream().collect(Collectors.toMap(SwitchConfig::getKey, Function.identity()));
		if (collect.get(switchConfig.getKey()) == null) {
			switchConfigs.add(switchConfig);
		} else {
			switchConfigs.forEach(config -> {
				if (config.getKey().equals(switchConfig.getKey())) {
					BeanUtils.copyProperties(switchConfig, config);
				}
			});
		}
		sysConfigEntity.setParamValue(JSON.toJSONString(switchConfigs));
		baseMapper.updateById(sysConfigEntity);
		return JsonResult.success();
	}

	@Override
	public JsonResult saveWxConfig(WxConfig wxConfig) {
		SysConfigEntity sysConfigEntity = baseMapper.queryByKey(ConfigConstant.WX_CONFIG);
		if (sysConfigEntity == null) {
			return JsonResult.fail("配置为空");
		}
		sysConfigEntity.setParamValue(JSON.toJSONString(wxConfig));
		baseMapper.updateById(sysConfigEntity);
		return JsonResult.success();
	}

	@Override
	public JsonResult saveAliyunSmsConfig(AliyunSmsConfig aliyunSmsConfig) {
		SysConfigEntity sysConfigEntity = baseMapper.queryByKey(ConfigConstant.ALI_YUN_SMS_CONFIG);
		if (sysConfigEntity == null) {
			return JsonResult.fail("配置为空");
		}
		sysConfigEntity.setParamValue(JSON.toJSONString(aliyunSmsConfig));
		baseMapper.updateById(sysConfigEntity);
		return JsonResult.success();
	}

	@Override
	public JsonResult saveReleaseConfig(ReleaseConfig releaseConfig) {
		SysConfigEntity sysConfigEntity = baseMapper.queryByKey(ConfigConstant.RELEASE_CONFIG);
		if (sysConfigEntity == null) {
			return JsonResult.fail("配置为空");
		}
		sysConfigEntity.setParamValue(JSON.toJSONString(releaseConfig));
		baseMapper.updateById(sysConfigEntity);
		return JsonResult.success();
	}

	@Override
	public JsonResult saveWebsiteConfig(WebsiteConfig websiteConfig) {
		SysConfigEntity sysConfigEntity = baseMapper.queryByKey(ConfigConstant.WEBSITE_CONFIG);
		if (sysConfigEntity == null) {
			return JsonResult.fail("配置为空");
		}
		sysConfigEntity.setParamValue(JSON.toJSONString(websiteConfig));
		baseMapper.updateById(sysConfigEntity);
		return JsonResult.success();
	}

	@Override
	public JsonResult saveQuickReplyConfig(QuickReplyConfig quickReplyConfig) {
		SysConfigEntity sysConfigEntity = baseMapper.queryByKey(ConfigConstant.QUICK_REPLY_CONFIG);
		if (sysConfigEntity == null) {
			return JsonResult.fail("配置为空");
		}
		List<QuickReplyConfig> quickReplyConfigs = JSON.parseArray(sysConfigEntity.getParamValue(), QuickReplyConfig.class);
		if (StringUtils.isEmpty(quickReplyConfig.getId())) {
			quickReplyConfig.setId(IdUtil.objectId());
			quickReplyConfigs.add(quickReplyConfig);
		} else {
			quickReplyConfigs.forEach(config -> {
				if (config.getId().equals(quickReplyConfig.getId())) {
					BeanUtils.copyProperties(quickReplyConfig, config);
				}
			});
		}
		sysConfigEntity.setParamValue(JSON.toJSONString(quickReplyConfigs));
		baseMapper.updateById(sysConfigEntity);
		return JsonResult.success();
	}

	@Override
	public JsonResult saveChatCardConfig(ChatCardConfig chatCardConfig) {
		SysConfigEntity sysConfigEntity = baseMapper.queryByKey(ConfigConstant.CHAT_CARD_CONFIG);
		if (sysConfigEntity == null) {
			return JsonResult.fail("配置为空");
		}
		List<ChatCardConfig> chatCardConfigs = JSON.parseArray(sysConfigEntity.getParamValue(), ChatCardConfig.class);
		Map<String, ChatCardConfig> collect = chatCardConfigs.stream().collect(Collectors.toMap(ChatCardConfig::getCode, Function.identity()));
		if (collect.get(chatCardConfig.getCode()) == null) {
			chatCardConfigs.add(chatCardConfig);
		} else {
			chatCardConfigs.forEach(config -> {
				if (config.getCode().equals(chatCardConfig.getCode())) {
					BeanUtils.copyProperties(chatCardConfig, config);
				}
			});
		}
		sysConfigEntity.setParamValue(JSON.toJSONString(chatCardConfigs));
		baseMapper.updateById(sysConfigEntity);
		return JsonResult.success();
	}

	@Override
	public List<ChatCardConfig> getChatCardConfig(Integer auth, Integer status) {
		SysConfigEntity sysConfigEntity = baseMapper.queryByKey(ConfigConstant.CHAT_CARD_CONFIG);
		if (sysConfigEntity == null) {
			return null;
		}
		List<ChatCardConfig> chatCardConfigs = JSON.parseArray(sysConfigEntity.getParamValue(), ChatCardConfig.class);
		if (status != null) {
			chatCardConfigs = chatCardConfigs.stream().filter(chatCardConfig -> status.equals(chatCardConfig.getStatus())).collect(Collectors.toList());
		}
		if (auth != null) {
			chatCardConfigs = chatCardConfigs.stream().filter(chatCardConfig -> auth.equals(chatCardConfig.getAuth())).collect(Collectors.toList());
		}
		return chatCardConfigs;
	}

	@Override
	public List<QuickReplyConfig> getQuickReplyConfig(Integer type, Integer status) {
		SysConfigEntity sysConfigEntity = baseMapper.queryByKey(ConfigConstant.QUICK_REPLY_CONFIG);
		if (sysConfigEntity == null) {
			return null;
		}
		List<QuickReplyConfig> quickReplyConfigs = JSON.parseArray(sysConfigEntity.getParamValue(), QuickReplyConfig.class);
		if (status != null) {
			quickReplyConfigs = quickReplyConfigs.stream().filter(quickReplyConfig -> status.equals(quickReplyConfig.getStatus())).collect(Collectors.toList());
		}
		if (type != null) {
			switch (type) {
				case 0:
					quickReplyConfigs = quickReplyConfigs.stream().filter(config -> config.getType().equals(0) || config.getType().equals(1)).collect(Collectors.toList());
					break;
				case 1:
					quickReplyConfigs = quickReplyConfigs.stream().filter(config -> config.getType().equals(0) || config.getType().equals(1) || config.getType().equals(2) ).collect(Collectors.toList());
					break;
				case 2:
					quickReplyConfigs = quickReplyConfigs.stream().filter(config -> config.getType().equals(0) || config.getType().equals(1) || config.getType().equals(3) ).collect(Collectors.toList());
					break;
				default:
					break;
			}
		}
		return quickReplyConfigs;
	}

	@Override
	public WebsiteConfig getWebsiteConfig() {
		SysConfigEntity sysConfigEntity = baseMapper.queryByKey(ConfigConstant.WEBSITE_CONFIG);
		if (sysConfigEntity == null) {
			return null;
		}
		return JSON.parseObject(sysConfigEntity.getParamValue(), WebsiteConfig.class);
	}

	@Override
	public List<KingKongConfig> getKingKongConfig(Integer status) {
		SysConfigEntity sysConfigEntity = baseMapper.queryByKey(ConfigConstant.KING_KONG_CONFIG);
		if (sysConfigEntity == null) {
			return null;
		}
		List<KingKongConfig> kingKongConfigs = JSON.parseArray(sysConfigEntity.getParamValue(), KingKongConfig.class);
		if (status != null) {
			kingKongConfigs = kingKongConfigs.stream().filter(kingKongConfig -> status.equals(kingKongConfig.getStatus())).collect(Collectors.toList());
		}
		return kingKongConfigs;
	}

	@Override
	public List<NewcomerGuideConfig> getNewcomerGuideConfig(Integer status) {
		SysConfigEntity sysConfigEntity = baseMapper.queryByKey(ConfigConstant.NEWCOMER_GUIDE_CONFIG);
		if (sysConfigEntity == null) {
			return null;
		}
		List<NewcomerGuideConfig> newcomerGuideConfigs = JSON.parseArray(sysConfigEntity.getParamValue(), NewcomerGuideConfig.class);
		if (status != null) {
			newcomerGuideConfigs = newcomerGuideConfigs.stream().filter(kingKongConfig -> status.equals(kingKongConfig.getStatus())).collect(Collectors.toList());
		}
		return newcomerGuideConfigs;
	}

	@Override
	public List<SwitchConfig> getSwitchConfig(Boolean value) {
		SysConfigEntity sysConfigEntity = baseMapper.queryByKey(ConfigConstant.SWITCH_CONFIG);
		if (sysConfigEntity == null) {
			return null;
		}
		List<SwitchConfig> switchConfigs = JSON.parseArray(sysConfigEntity.getParamValue(), SwitchConfig.class);
		if (value != null) {
			switchConfigs = switchConfigs.stream().filter(kingKongConfig -> value.equals(kingKongConfig.getValue())).collect(Collectors.toList());
		}
		return switchConfigs;
	}

	@Override
	public WxConfig getWxConfig() {
		SysConfigEntity sysConfigEntity = baseMapper.queryByKey(ConfigConstant.WX_CONFIG);
		if (sysConfigEntity == null) {
			return null;
		}
		return JSON.parseObject(sysConfigEntity.getParamValue(), WxConfig.class);
	}

	@Override
	public AliyunSmsConfig getAliyunSmsConfig() {
		SysConfigEntity sysConfigEntity = baseMapper.queryByKey(ConfigConstant.ALI_YUN_SMS_CONFIG);
		if (sysConfigEntity == null) {
			return null;
		}
		return JSON.parseObject(sysConfigEntity.getParamValue(), AliyunSmsConfig.class);
	}

	@Override
	public ReleaseConfig getReleaseConfig() {
		SysConfigEntity sysConfigEntity = baseMapper.queryByKey(ConfigConstant.RELEASE_CONFIG);
		if (sysConfigEntity == null) {
			return null;
		}
		ReleaseConfig releaseConfig = JSON.parseObject(sysConfigEntity.getParamValue(), ReleaseConfig.class);
		int count = demandService.count();
		if (releaseConfig.getBaseCount() != null) {
			releaseConfig.setBaseCount(releaseConfig.getBaseCount() + count);
		}
		return releaseConfig;
	}

	@Override
	public KingKongConfig getKingKongConfig(String type) {
		SysConfigEntity sysConfigEntity = baseMapper.queryByKey(ConfigConstant.KING_KONG_CONFIG);
		if (sysConfigEntity == null) {
			return null;
		}
		List<KingKongConfig> kingKongConfigs = JSON.parseArray(sysConfigEntity.getParamValue(), KingKongConfig.class);
		Map<String, KingKongConfig> map = kingKongConfigs.stream().collect(Collectors.toMap(KingKongConfig::getType, Function.identity()));
		return map.get(type);
	}

	@Override
	public NewcomerGuideConfig getNewcomerGuideConfig(String type) {
		SysConfigEntity sysConfigEntity = baseMapper.queryByKey(ConfigConstant.NEWCOMER_GUIDE_CONFIG);
		if (sysConfigEntity == null) {
			return null;
		}
		List<NewcomerGuideConfig> newcomerGuideConfigs = JSON.parseArray(sysConfigEntity.getParamValue(), NewcomerGuideConfig.class);
		Map<String, NewcomerGuideConfig> map = newcomerGuideConfigs.stream().collect(Collectors.toMap(NewcomerGuideConfig::getType, Function.identity()));
		return map.get(type);
	}

	@Override
	public SwitchConfig getSwitchConfig(String type) {
		SysConfigEntity sysConfigEntity = baseMapper.queryByKey(ConfigConstant.SWITCH_CONFIG);
		if (sysConfigEntity == null) {
			return null;
		}
		List<SwitchConfig> switchConfigs = JSON.parseArray(sysConfigEntity.getParamValue(), SwitchConfig.class);
		Map<String, SwitchConfig> map = switchConfigs.stream().collect(Collectors.toMap(SwitchConfig::getKey, Function.identity()));
		return map.get(type);
	}

	@Override
	public JsonResult removeNewcomerGuideConfig(String type) {
		SysConfigEntity sysConfigEntity = baseMapper.queryByKey(ConfigConstant.NEWCOMER_GUIDE_CONFIG);
		List<NewcomerGuideConfig> newcomerGuideConfigs = JSON.parseArray(sysConfigEntity.getParamValue(), NewcomerGuideConfig.class);
		Iterator it = newcomerGuideConfigs.iterator();
		while(it.hasNext()) {
			NewcomerGuideConfig newcomerGuideConfig = (NewcomerGuideConfig) it.next();
			if (newcomerGuideConfig.getType().equals(type)) {
				it.remove();
			}
		}
		sysConfigEntity.setParamValue(JSON.toJSONString(newcomerGuideConfigs));
		baseMapper.updateById(sysConfigEntity);
		return JsonResult.success();
	}

	@Override
	public JsonResult removeSwitchConfig(String key) {
		SysConfigEntity sysConfigEntity = baseMapper.queryByKey(ConfigConstant.SWITCH_CONFIG);
		List<SwitchConfig> switchConfigs = JSON.parseArray(sysConfigEntity.getParamValue(), SwitchConfig.class);
		Iterator it = switchConfigs.iterator();
		while(it.hasNext()) {
			SwitchConfig switchConfig = (SwitchConfig) it.next();
			if (switchConfig.getKey().equals(key)) {
				it.remove();
			}
		}
		sysConfigEntity.setParamValue(JSON.toJSONString(switchConfigs));
		baseMapper.updateById(sysConfigEntity);
		return JsonResult.success();
	}

	@Override
	public JsonResult removeQuickReplyConfig(String id) {
		SysConfigEntity sysConfigEntity = baseMapper.queryByKey(ConfigConstant.QUICK_REPLY_CONFIG);
		List<QuickReplyConfig> quickReplyConfigs = JSON.parseArray(sysConfigEntity.getParamValue(), QuickReplyConfig.class);
		Iterator it = quickReplyConfigs.iterator();
		while(it.hasNext()) {
			QuickReplyConfig quickReplyConfig = (QuickReplyConfig) it.next();
			if (quickReplyConfig.getId().equals(id)) {
				it.remove();
			}
		}
		sysConfigEntity.setParamValue(JSON.toJSONString(quickReplyConfigs));
		baseMapper.updateById(sysConfigEntity);
		return JsonResult.success();
	}

	@Override
	public JsonResult sortNewcomerGuideConfig(List<String> types) {
		SysConfigEntity sysConfigEntity = baseMapper.queryByKey(ConfigConstant.NEWCOMER_GUIDE_CONFIG);
		List<NewcomerGuideConfig> newcomerGuideConfigs = JSON.parseArray(sysConfigEntity.getParamValue(), NewcomerGuideConfig.class);
		Map<String, NewcomerGuideConfig> collect = newcomerGuideConfigs.stream().collect(Collectors.toMap(NewcomerGuideConfig::getType, Function.identity()));
		List<NewcomerGuideConfig> list = new ArrayList<>();
		for (String type : types) {
			if (collect.get(type) == null) {
				return JsonResult.fail("类型错误");
			} else {
				list.add(collect.get(type));
			}
		}
		sysConfigEntity.setParamValue(JSON.toJSONString(list));
		baseMapper.updateById(sysConfigEntity);
		return JsonResult.success();
	}

	@Override
	public JsonResult sortQuickReplyConfig(List<String> ids) {
		SysConfigEntity sysConfigEntity = baseMapper.queryByKey(ConfigConstant.QUICK_REPLY_CONFIG);
		List<QuickReplyConfig> quickReplyConfigs = JSON.parseArray(sysConfigEntity.getParamValue(), QuickReplyConfig.class);
		Map<String, QuickReplyConfig> collect = quickReplyConfigs.stream().collect(Collectors.toMap(QuickReplyConfig::getId, Function.identity()));
		List<QuickReplyConfig> list = new ArrayList<>();
		for (String id : ids) {
			if (collect.get(id) == null) {
				return JsonResult.fail("ID错误");
			} else {
				list.add(collect.get(id));
			}
		}
		sysConfigEntity.setParamValue(JSON.toJSONString(list));
		baseMapper.updateById(sysConfigEntity);
		return JsonResult.success();
	}

}
