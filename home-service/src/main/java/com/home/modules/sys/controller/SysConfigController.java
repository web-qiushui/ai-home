package com.home.modules.sys.controller;


import com.home.common.annotation.SysLog;
import com.home.common.constant.config.*;
import com.home.common.utils.JsonResult;
import com.home.common.utils.PageUtils;
import com.home.common.utils.R;
import com.home.common.validator.ValidatorUtils;
import com.home.modules.sys.entity.SysConfigEntity;
import com.home.modules.sys.service.SysConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 系统配置信息
 *
 * @author xw
 */
@Api(value = "系统配置接口", tags = {"系统配置接口"})
@RestController
@RequestMapping("/sys/config")
public class SysConfigController extends AbstractController {

	@Resource
	private SysConfigService sysConfigService;
	
	/**
	 * 所有配置列表
	 */
	@GetMapping("/list")
	@RequiresPermissions("sys:config:list")
	public R list(@RequestParam Map<String, Object> params){
		PageUtils page = sysConfigService.queryPage(params);

		return R.ok().put("page", page);
	}
	
	
	/**
	 * 配置信息
	 */
	@GetMapping("/info/{id}")
	@RequiresPermissions("sys:config:info")
	public R info(@PathVariable("id") Long id){
		SysConfigEntity config = sysConfigService.getById(id);
		
		return R.ok().put("config", config);
	}
	
	/**
	 * 保存配置
	 */
	@SysLog("保存配置")
	@PostMapping("/save")
	@RequiresPermissions("sys:config:save")
	public R save(@RequestBody SysConfigEntity config){
		ValidatorUtils.validateEntity(config);
		config.setCreateBy(getUserId().intValue());
		config.setIsDelete(1);
		sysConfigService.saveConfig(config);
		
		return R.ok();
	}
	
	/**
	 * 修改配置
	 */
	@SysLog("修改配置")
	@PostMapping("/update")
	@RequiresPermissions("sys:config:update")
	public R update(@RequestBody SysConfigEntity config){
		ValidatorUtils.validateEntity(config);
		
		sysConfigService.update(config);
		
		return R.ok();
	}
	
	/**
	 * 删除配置
	 */
	@SysLog("删除配置")
	@PostMapping("/delete")
	@RequiresPermissions("sys:config:delete")
	public R delete(@RequestBody Long[] ids){
		sysConfigService.deleteBatch(ids);
		
		return R.ok();
	}

	@SysLog("修改或新增金刚区")
	@ApiOperation("修改或新增金刚区")
	@PostMapping("saveKingKongConfig")
	JsonResult saveKingKongConfig(@RequestBody @Valid KingKongConfig kingKongConfig) {
		return sysConfigService.saveKingKongConfig(kingKongConfig);
	}

	@SysLog("修改或新增新人指引")
	@ApiOperation("修改或新增新人指引")
	@PostMapping("saveNewcomerGuideConfig")
	JsonResult saveNewcomerGuideConfig(@RequestBody @Valid NewcomerGuideConfig newcomerGuideConfig) {
		return sysConfigService.saveNewcomerGuideConfig(newcomerGuideConfig);
	}

	@SysLog("修改或新增开关配置")
	@ApiOperation("修改或新增开关配置")
	@PostMapping("saveSwitchConfig")
	JsonResult saveSwitchConfig(@RequestBody @Valid SwitchConfig switchConfig) {
		return sysConfigService.saveSwitchConfig(switchConfig);
	}

	@SysLog("修改或新增微信配置")
	@ApiOperation("修改或新增微信配置")
	@PostMapping("saveWxConfig")
	JsonResult saveWxConfig(@RequestBody @Valid WxConfig wxConfig) {
		return sysConfigService.saveWxConfig(wxConfig);
	}

	@SysLog("修改或新增阿里云短信")
	@ApiOperation("修改或新增阿里云短信")
	@PostMapping("saveAliyunSmsConfig")
	JsonResult saveAliyunSmsConfig(@RequestBody @Valid AliyunSmsConfig aliyunSmsConfig) {
		return sysConfigService.saveAliyunSmsConfig(aliyunSmsConfig);
	}

	@SysLog("修改或新增购房需求配置")
	@ApiOperation("修改或新增购房需求配置")
	@PostMapping("saveReleaseConfig")
	JsonResult saveReleaseConfig(@RequestBody @Valid ReleaseConfig releaseConfig) {
		return sysConfigService.saveReleaseConfig(releaseConfig);
	}

	@SysLog("修改或新增网站配置")
	@ApiOperation("修改或新增网站配置")
	@PostMapping("saveWebsiteConfig")
	JsonResult saveWebsiteConfig(@RequestBody @Valid WebsiteConfig websiteConfig) {
		return sysConfigService.saveWebsiteConfig(websiteConfig);
	}

	@SysLog("修改或新增快速回复配置")
	@ApiOperation("修改或新增快速回复配置")
	@PostMapping("saveQuickReplyConfig")
	JsonResult saveQuickReplyConfig(@RequestBody @Valid QuickReplyConfig quickReplyConfig) {
		return sysConfigService.saveQuickReplyConfig(quickReplyConfig);
	}

	@SysLog("修改或新增聊天卡片配置")
	@ApiOperation("修改或新增聊天卡片配置")
	@PostMapping("saveChatCardConfig")
	JsonResult saveChatCardConfig(@RequestBody @Valid ChatCardConfig chatCardConfig) {
		return sysConfigService.saveChatCardConfig(chatCardConfig);
	}


	@ApiOperation("查询聊天卡片配置")
	@GetMapping("getChatCardConfig")
	JsonResult<List<ChatCardConfig>> getChatCardConfig(Integer auth, Integer status) {
		List<ChatCardConfig> chatCardConfigs = sysConfigService.getChatCardConfig(auth, status);
		return JsonResult.success(chatCardConfigs);
	}

	@ApiOperation("查询快速回复配置")
	@GetMapping("/anon/getQuickReplyConfig")
	JsonResult<List<QuickReplyConfig>> getQuickReplyConfig(@ApiParam("权限 0 用户 1 AI 2置业")Integer type, @ApiParam("状态（0 禁用 1 启用）")Integer status) {
		List<QuickReplyConfig> quickReplyConfigs = sysConfigService.getQuickReplyConfig(type, status);
		return JsonResult.success(quickReplyConfigs);
	}

	@ApiOperation("查询网站配置")
	@GetMapping("/anon/getWebsiteConfig")
	JsonResult<WebsiteConfig> getWebsiteConfig() {
		WebsiteConfig websiteConfig = sysConfigService.getWebsiteConfig();
		return JsonResult.success(websiteConfig);
	}

	@ApiOperation("查询金刚区内容")
	@GetMapping("/anon/getKingKongConfig")
	JsonResult<List<KingKongConfig>> getKingKongConfig(@ApiParam("状态（0 禁用 1 启用）")Integer status) {
		List<KingKongConfig> kingKongConfig = sysConfigService.getKingKongConfig(status);
		return JsonResult.success(kingKongConfig);
	}

	@ApiOperation("查询新人指引")
	@GetMapping("/anon/getNewcomerGuideConfig")
	JsonResult<List<NewcomerGuideConfig>> getNewcomerGuideConfig(@ApiParam("状态（0 禁用 1 启用）") Integer status) {
		List<NewcomerGuideConfig> newcomerGuideConfig = sysConfigService.getNewcomerGuideConfig(status);
		return JsonResult.success(newcomerGuideConfig);
	}

	@ApiOperation("查询购房需求配置")
	@GetMapping("/anon/getReleaseConfig")
	JsonResult<ReleaseConfig> getReleaseConfig() {
		ReleaseConfig releaseConfig = sysConfigService.getReleaseConfig();
		return JsonResult.success(releaseConfig);
	}

	@ApiOperation("查询开关配置")
	@GetMapping("getSwitchConfig")
	JsonResult<List<SwitchConfig>> getSwitchConfig(@ApiParam("开关状态（false 禁用 true 启用）") Boolean value) {
		List<SwitchConfig> switchConfig = sysConfigService.getSwitchConfig(value);
		return JsonResult.success(switchConfig);
	}

	@ApiOperation("查询微信配置")
	@GetMapping("getWxConfig")
	JsonResult<WxConfig> getWxConfig() {
		WxConfig wxConfig = sysConfigService.getWxConfig();
		return JsonResult.success(wxConfig);
	}

	@ApiOperation("查询阿里云短信配置")
	@GetMapping("getAliyunSmsConfig")
	JsonResult<AliyunSmsConfig> getAliyunSmsConfig() {
		AliyunSmsConfig aliyunSmsConfig = sysConfigService.getAliyunSmsConfig();
		return JsonResult.success(aliyunSmsConfig);
	}

	@ApiOperation("删除新人指引")
	@GetMapping("removeNewcomerGuideConfig")
	JsonResult removeNewcomerGuideConfig(@RequestParam String type) {
		return sysConfigService.removeNewcomerGuideConfig(type);
	}

	@ApiOperation("删除开关配置")
	@GetMapping("removeSwitchConfig")
	JsonResult removeSwitchConfig(@RequestParam String key) {
		return sysConfigService.removeSwitchConfig(key);
	}

	@ApiOperation("删除快速回复配置")
	@GetMapping("removeQuickReplyConfig")
	JsonResult removeQuickReplyConfig(@RequestParam String id) {
		return sysConfigService.removeQuickReplyConfig(id);
	}

	@ApiOperation("排序新人指引")
	@GetMapping("sortNewcomerGuideConfig")
	JsonResult sortNewcomerGuideConfig(@RequestParam List<String> types) {
		return sysConfigService.sortNewcomerGuideConfig(types);
	}

	@ApiOperation("排序快速回复配置")
	@GetMapping("sortQuickReplyConfig")
	JsonResult sortQuickReplyConfig(@RequestParam List<String> ids) {
		return sysConfigService.sortQuickReplyConfig(ids);
	}

}
