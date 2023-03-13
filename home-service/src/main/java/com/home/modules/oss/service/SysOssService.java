package com.home.modules.oss.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.home.common.utils.JsonResult;
import com.home.common.utils.PageUtils;
import com.home.modules.oss.entity.SysOssEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 文件上传
 *
 * @author xw
 */
public interface SysOssService extends IService<SysOssEntity> {

	PageUtils queryPage(Map<String, Object> params);

	/**
	 * 上传文件
	 */
	JsonResult uploadImages(MultipartFile file);
}
