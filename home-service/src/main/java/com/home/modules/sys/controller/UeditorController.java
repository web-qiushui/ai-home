package com.home.modules.sys.controller;

import com.alibaba.fastjson.JSON;
import com.home.common.utils.JsonResult;
import com.home.common.utils.UeditorBean;
import com.home.common.vo.UeditorVO;
import com.home.modules.oss.service.SysOssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * 百度Controller
 * @author xw
 * @date 2020-08-12
 */
@Slf4j
@Api(value = "百度编辑器", tags = {"百度编辑器"})
@RestController
public class UeditorController{

    /**
     * 配置
     */
    private final String CONFIG = "config";

    /**
	 * 图片上传
	 */
	private final String UPLOADIMAGE = "uploadimage";

	/**
	 * 视频上传
	 */
	private final String UPLOADVIDEO = "uploadvideo";

	@Value("${service.http}")
	private String http;

	@Value("${service.img.access}")
	private String access;

	@Resource
	private SysOssService sysOssService;

	@ApiOperation(value = "百度编辑器配置", notes = "百度编辑器配置")
	@RequestMapping("/ueditor")
	public Object ueditor(String action, MultipartFile upfile) {
		if (CONFIG.equals(action)) {
			return JSON.parseObject(UeditorBean.UEDITOR_CONFIG);
		}
		// 图片-视频（音频）上传
		if (UPLOADIMAGE.equals(action) || UPLOADVIDEO.equals(action) ) {
			try {
				JsonResult jsonResult = sysOssService.uploadImages(upfile);
				String url = http + jsonResult.getData();
				return new UeditorVO("SUCCESS", url, "图片", "");
			} catch (Exception e) {
				log.error("文件上传失败", e);
				e.printStackTrace();
			}
			return null;
		}
		return UeditorBean.UeditorMsg.ERROR.get();
	}
}
