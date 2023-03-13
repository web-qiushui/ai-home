package com.home.common.xss;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * XSS过滤
 *
 * @author xw
 */
@Slf4j
public class XssFilter implements Filter {

	@Override
	public void init(FilterConfig config) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper(
				(HttpServletRequest) request);
		// 如果通过nginx代理，那么打印打印真实地址
		String ip = xssRequest.getHeader("X-real-ip")  == null ? xssRequest.getRemoteAddr() : xssRequest.getHeader("X-real-ip") ;
		String method = xssRequest.getMethod();
		String requestUri = xssRequest.getRequestURI();
		String queryString = xssRequest.getQueryString();
		//非json类型，直接返回
		if(!MediaType.APPLICATION_JSON_VALUE.equalsIgnoreCase(xssRequest.getHeader(HttpHeaders.CONTENT_TYPE))){
			log.info("请求方法：{}， 客户端IP：{}， 请求方式：{}， 请求参数：{}", requestUri, ip, method, queryString);
		}
		chain.doFilter(xssRequest, response);
	}

	@Override
	public void destroy() {
	}

}