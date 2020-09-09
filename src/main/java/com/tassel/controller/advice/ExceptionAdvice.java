package com.tassel.controller.advice;

import com.tassel.util.CommunityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author shuaiyin.zhang
 * @description
 * @date 2020/09/09
 */
@ControllerAdvice(annotations = Controller.class) //只对标注Controller注解的类进行处理
public class ExceptionAdvice {

	private static final Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);

	@ExceptionHandler({Exception.class}) //注解参数是拦截的异常类型
	public void handleException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.error("服务器发生异常: {}", e.getMessage());
		for (StackTraceElement element : e.getStackTrace()) {
			logger.error(element.toString());
		}
		String header = request.getHeader("x-requested-with");
		// 当前请求为异步请求
		if ("XMLHttpRequest".equals(header)) {
			response.setContentType("application/json;charset=utf-8");
			PrintWriter writer = response.getWriter();
			writer.write(CommunityUtil.getJSONString(1, "服务器异常!"));
		} else {
			// 请求为网页请求
			response.sendRedirect(CommunityUtil.contextPathJudge(request.getContextPath()) + "/error");
		}

	}
}
