package com.home.modules.oss.controller;

import com.google.gson.Gson;
import com.home.common.constant.ConfigConstant;
import com.home.common.exception.RRException;
import com.home.common.utils.Constant;
import com.home.common.utils.JsonResult;
import com.home.common.utils.PageUtils;
import com.home.common.utils.R;
import com.home.common.validator.ValidatorUtils;
import com.home.common.validator.group.AliyunGroup;
import com.home.common.validator.group.QcloudGroup;
import com.home.common.validator.group.QiniuGroup;
import com.home.modules.oss.cloud.CloudStorageConfig;
import com.home.modules.oss.cloud.OSSFactory;
import com.home.modules.oss.entity.SysOssEntity;
import com.home.modules.oss.service.SysOssService;
import com.home.modules.sys.service.SysConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * 文件上传
 *
 * @author xw
 */
@Slf4j
@Api(value = "WEB接口", tags = {"WEB相关接口"})
@RestController
@RequestMapping("sys/oss")
public class SysOssController {
	@Resource
	private SysOssService sysOssService;
    @Resource
    private SysConfigService sysConfigService;
	/**
	 * 是否开启本地存储  true开启   false关闭
	 */
	@Value("${service.file.enable: true}")
	private boolean open;

    private final static String KEY = ConfigConstant.CLOUD_STORAGE_CONFIG_KEY;
	
	/**
	 * 列表
	 */
	@GetMapping("/list")
	@RequiresPermissions("sys:oss:all")
	public R list(@RequestParam Map<String, Object> params){
		PageUtils page = sysOssService.queryPage(params);

		return R.ok().put("page", page);
	}


    /**
     * 云存储配置信息
     */
    @GetMapping("/config")
    @RequiresPermissions("sys:oss:all")
    public R config(){
        CloudStorageConfig config = sysConfigService.getConfigObject(KEY, CloudStorageConfig.class);

        return R.ok().put("config", config);
    }


	/**
	 * 保存云存储配置信息
	 */
	@PostMapping("/saveConfig")
	@RequiresPermissions("sys:oss:all")
	public R saveConfig(@RequestBody CloudStorageConfig config){
		//校验类型
		ValidatorUtils.validateEntity(config);

		if(config.getType() == Constant.CloudService.QINIU.getValue()){
			//校验七牛数据
			ValidatorUtils.validateEntity(config, QiniuGroup.class);
		}else if(config.getType() == Constant.CloudService.ALIYUN.getValue()){
			//校验阿里云数据
			ValidatorUtils.validateEntity(config, AliyunGroup.class);
		}else if(config.getType() == Constant.CloudService.QCLOUD.getValue()){
			//校验腾讯云数据
			ValidatorUtils.validateEntity(config, QcloudGroup.class);
		}

        sysConfigService.updateValueByKey(KEY, new Gson().toJson(config));

		return R.ok();
	}
	

	/**
	 * 上传文件
	 */
	@PostMapping("/upload")
	@RequiresPermissions("sys:oss:all")
	public R upload(@RequestParam("file") MultipartFile file) throws Exception {
		if (file.isEmpty()) {
			throw new RRException("上传文件不能为空");
		}
		String url;
		if (open) {
			JsonResult jsonResult = sysOssService.uploadImages(file);
			if (jsonResult.isSuccess()) {
				url = jsonResult.getData().toString();
			}else {
				return R.error(jsonResult.getMsg());
			}
		} else {
			//上传文件
			String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
			url = OSSFactory.build().uploadSuffix(file.getBytes(), suffix);
			//保存文件信息
			SysOssEntity ossEntity = new SysOssEntity();
			ossEntity.setUrl(url);
			ossEntity.setCreateDate(new Date());
			sysOssService.save(ossEntity);
		}
		return R.ok().put("url", url);
	}

	@ApiOperation("上传文件")
	@PostMapping("/upload/images")
	public JsonResult uploadImages(@RequestParam("file") MultipartFile file) {
		return sysOssService.uploadImages(file);
	}


	/**
	 * 删除
	 */
	@PostMapping("/delete")
	@RequiresPermissions("sys:oss:all")
	public R delete(@RequestBody Long[] ids){
		sysOssService.removeByIds(Arrays.asList(ids));

		return R.ok();
	}

}
