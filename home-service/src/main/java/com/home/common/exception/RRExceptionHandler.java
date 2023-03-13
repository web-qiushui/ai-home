package com.home.common.exception;

import com.home.common.utils.JsonResult;
import com.home.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.stream.Collectors;

/**
 * 异常处理器
 *
 * @author xw
 */
@Slf4j
@RestControllerAdvice
public class RRExceptionHandler {

	/**
	 * 处理自定义异常
	 */
	@ExceptionHandler(RRException.class)
	public JsonResult handleRRException(RRException e){
		log.error("自定义异常处理", e);
		return JsonResult.fail(e.getCode(), e.getMessage());
	}

	/**
	 * 参数校验
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public JsonResult handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
		String message = e.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining());
		return JsonResult.fail(message);
	}

	/**
	 * 统一异常处理
	 */
	@ExceptionHandler(Exception.class)
	public JsonResult handleException(Exception e){
		log.error(e.getMessage(), e);
		return JsonResult.fail("未知异常，请联系管理员");
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	public JsonResult handlerNoFoundException(Exception e) {
		log.error(e.getMessage(), e);
		return JsonResult.fail(404, "路径不存在，请检查路径是否正确");
	}

	@ExceptionHandler(DuplicateKeyException.class)
	public JsonResult handleDuplicateKeyException(DuplicateKeyException e){
		log.error(e.getMessage(), e);
		return JsonResult.fail("数据库中已存在该记录");
	}

	@ExceptionHandler(AuthorizationException.class)
	public JsonResult handleAuthorizationException(AuthorizationException e){
		log.error(e.getMessage(), e);
		return JsonResult.fail("没有权限，请联系管理员授权");
	}
}
