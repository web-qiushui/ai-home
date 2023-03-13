package com.home.modules.oss.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.home.common.exception.RRException;
import com.home.common.utils.FileStore;
import com.home.common.utils.JsonResult;
import com.home.common.utils.PageUtils;
import com.home.common.utils.Query;
import com.home.modules.oss.dao.SysOssDao;
import com.home.modules.oss.entity.SysOssEntity;
import com.home.modules.oss.service.SysOssService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Slf4j
@Service("sysOssService")
public class SysOssServiceImpl extends ServiceImpl<SysOssDao, SysOssEntity> implements SysOssService {

	@Value("${service.img.path}")
	private String imgPath;
	@Value("${service.img.access}")
	private String imgAccess;
	@Value("#{'${service.img.format}'.split(',')}")
	private List<String> imgFormat;

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		IPage<SysOssEntity> page = this.page(
			new Query<SysOssEntity>().getPage(params)
		);

		return new PageUtils(page);
	}

	@Override
	public JsonResult uploadImages(MultipartFile file) {
		if (file.isEmpty()) {
			throw new RRException("上传文件不能为空");
		}
		// 获取文件名
		String fileName = file.getOriginalFilename();
		log.info("上传的文件名为：{}", fileName);
		// 获取文件的后缀名
		String suffixName = fileName.substring(fileName.lastIndexOf("."));
		if (!imgFormat.contains(suffixName)) {
			return JsonResult.fail("文件格式错误");
		}
		// 获取文件夹
		String folder = suffixName.replace(".","");
		log.info("文件的后缀名为：{}", suffixName);
		fileName = FileStore.generateRandomFilename();
		// 设置文件存储路径
		String path = imgPath + folder + fileName + suffixName;
		String requestPath = imgAccess + folder + fileName + suffixName;
		boolean isUpload = FileStore.uploadBase64(file, path);
		if (!isUpload) {
			return JsonResult.fail("文件上传失败");
		} else {
			//保存文件信息
			SysOssEntity ossEntity = new SysOssEntity();
			ossEntity.setUrl(requestPath);
			ossEntity.setCreateDate(new Date());
			baseMapper.insert(ossEntity);
		}
		return JsonResult.success(requestPath);
	}

}
