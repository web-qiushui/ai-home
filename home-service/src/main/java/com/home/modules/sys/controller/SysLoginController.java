package com.home.modules.sys.controller;

import com.home.common.annotation.NoRepeatSubmit;
import com.home.common.dto.CaptchaDTO;
import com.home.common.utils.JsonResult;
import com.home.common.utils.R;
import com.home.common.validator.Assert;
import com.home.modules.sys.entity.SysUserEntity;
import com.home.modules.sys.form.SysLoginForm;
import com.home.modules.sys.service.SysCaptchaService;
import com.home.modules.sys.service.SysUserService;
import com.home.modules.sys.service.SysUserTokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

/**
 * 登录相关
 *
 * @author xw
 */
@Api(value = "WEB接口", tags = {"WEB相关接口"})
@RestController
public class SysLoginController extends AbstractController {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysUserTokenService sysUserTokenService;
	@Autowired
	private SysCaptchaService sysCaptchaService;

	/**
	 * 验证码
	 */
	@GetMapping("captcha.jpg")
	public void captcha(HttpServletResponse response, String uuid)throws IOException {
		response.setHeader("Cache-Control", "no-store, no-cache");
		response.setContentType("image/jpeg");

		//获取图片验证码
		BufferedImage image = sysCaptchaService.getCaptcha(uuid);

		ServletOutputStream out = response.getOutputStream();
		ImageIO.write(image, "jpg", out);
		IOUtils.closeQuietly(out);
	}


	@NoRepeatSubmit
	@ApiOperation("发送验证码")
	@GetMapping("/sys/anon/sendCaptcha")
	public JsonResult sendCaptcha(@Valid CaptchaDTO dto) {
		Assert.isNull(dto.getType(), "类型不能为空！");
		return sysCaptchaService.sendCaptcha(dto);
	}


	@NoRepeatSubmit
	@ApiOperation("校验验证码")
	@GetMapping("/sys/anon/checkCaptcha")
	public JsonResult checkCaptcha(@Valid CaptchaDTO dto) {
		Assert.isBlank(dto.getCode(), "验证码不能为空！");
		return sysCaptchaService.checkCaptcha(dto);
	}

	/**
	 * 登录
	 */
	@PostMapping("/sys/login")
	public Map<String, Object> login(@RequestBody SysLoginForm form) {
		boolean captcha = sysCaptchaService.validate(form.getUuid(), form.getCaptcha());
		if(!captcha){
			return R.error("验证码不正确");
		}
		//用户信息
		SysUserEntity user = sysUserService.queryByUserName(form.getUsername());

		//账号不存在、密码错误
		if(user == null || !user.getPassword().equals(new Sha256Hash(form.getPassword(), user.getSalt()).toHex())) {
			return R.error("账号或密码不正确");
		}

		//账号锁定
		if(user.getStatus() == 0){
			return R.error("账号已被锁定,请联系管理员");
		}

		//生成token，并保存到数据库
		R r = sysUserTokenService.createToken(user.getUserId());
		return r;
	}


	/**
	 * 退出
	 */
	@PostMapping("/sys/logout")
	public R logout() {
		sysUserTokenService.logout(getUserId());
		return R.ok();
	}
	
}
