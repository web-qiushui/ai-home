package com.home.modules.app.controller;

import com.home.common.annotation.NoRepeatSubmit;
import com.home.common.dto.UserDTO;
import com.home.common.dto.WxUserDTO;
import com.home.common.utils.JsonResult;
import com.home.common.utils.JwtUtil;
import com.home.common.validator.Assert;
import com.home.common.validator.ValidatorUtils;
import com.home.common.vo.UserVO;
import com.home.modules.app.annotation.Login;
import com.home.modules.app.entity.UserEntity;
import com.home.modules.app.form.LoginForm;
import com.home.modules.app.form.RegisterForm;
import com.home.modules.app.service.UserService;
import com.home.modules.app.utils.JwtUtils;
import com.home.modules.generator.entity.LoginEntity;
import com.home.modules.generator.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;

/**
 * APP用户模块
 *
 * @author xw
 */
@Slf4j
@RestController
@RequestMapping("/app/user")
@Api(value = "APP用户模块", tags = {"APP用户模块"})
public class AppUserController {

    @Resource
    private UserService userService;
    @Resource
    private JwtUtils jwtUtils;
    @Resource
    private LoginService loginService;

    /**
     * 刷新token
     */
    @NoRepeatSubmit
    @ApiOperation("刷新token")
    @GetMapping("refreshToken")
    public JsonResult refreshToken(@RequestParam String token, HttpServletRequest request){
        if (jwtUtils.verify(token)) {
            Integer userId = JwtUtil.getUserId();
            if (userId == null) {
                return JsonResult.fail("token失效，请重新登录");
            }
            LoginEntity loginEntity = new LoginEntity();
            loginEntity.setHomeUserId(userId);
            String ip = request.getHeader("X-real-ip")  == null ? request.getRemoteAddr() : request.getHeader("X-real-ip") ;
            loginEntity.setIp(ip);
            loginService.save(loginEntity);
            //生成token
            return JsonResult.success(jwtUtils.generateToken(userId.longValue()));
        } else {
            return JsonResult.fail("token失效，请重新登录");
        }
    }

    /**
     * 登录
     */
    @PostMapping("login")
    @ApiOperation("登录")
    public JsonResult login(@RequestBody LoginForm form){
        //表单校验
        ValidatorUtils.validateEntity(form);

        //用户登录
        long userId = userService.login(form);

        //生成token
        String token = jwtUtils.generateToken(userId);

        return JsonResult.success(token);
    }

    /**
     * 微信登录
     */
    @NoRepeatSubmit
    @ApiOperation("微信登录")
    @PostMapping("wxLogin")
    public JsonResult wxLogin(@RequestBody @Valid WxUserDTO dto, HttpServletRequest request){

        //用户登录
        Integer userId = userService.wxLogin(dto);

        //生成token
        String token = jwtUtils.generateToken(userId.longValue());

        LoginEntity loginEntity = new LoginEntity();
        loginEntity.setHomeUserId(userId);
        String ip = request.getHeader("X-real-ip")  == null ? request.getRemoteAddr() : request.getHeader("X-real-ip") ;
        loginEntity.setIp(ip);
        loginService.save(loginEntity);

        return JsonResult.success(token);
    }

    @NoRepeatSubmit
    @ApiOperation("注册")
    @PostMapping("register")
    public JsonResult register(@RequestBody RegisterForm form){
        //表单校验
        ValidatorUtils.validateEntity(form);

        UserEntity userEntity = userService.queryByMobile(form.getMobile());
        if (userEntity != null) {
            return JsonResult.fail("用户已注册");
        }
        UserEntity user = new UserEntity();
        user.setPhone(form.getMobile());
        user.setUserName(form.getMobile());
        user.setPassWord(DigestUtils.sha256Hex(form.getPassword()));
        user.setCreateTime(new Date());
        userService.save(user);
        //生成token
        String token = jwtUtils.generateToken(user.getId());
        return JsonResult.success(token);
    }

    @Login
    @ApiOperation("获取用户信息")
    @GetMapping("userInfo")
    public JsonResult<UserVO> userInfo() {
        Integer userId = JwtUtil.getUserId();
        UserVO userVO = userService.userInfo(userId);
        return JsonResult.success(userVO);
    }

    @Login
    @NoRepeatSubmit
    @PostMapping("updateUserInfo")
    @ApiOperation("修改用户信息")
    public JsonResult updateUserInfo(@RequestBody UserDTO dto){
        dto.setId(JwtUtil.getUserId());
        return userService.updateUserInfo(dto);
    }
}
