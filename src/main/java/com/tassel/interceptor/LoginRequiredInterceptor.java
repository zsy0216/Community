package com.tassel.interceptor;

import com.tassel.annotation.LoginRequired;
import com.tassel.util.CommunityUtil;
import com.tassel.util.HostHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author shuaiyin.zhang
 * @description
 * @date 2020/07/18
 */
@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {
	private static final Logger logger = LoggerFactory.getLogger(LoginRequiredInterceptor.class);

	@Resource
	private HostHolder hostHolder;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		logger.info("请求拦截: {}", request.getRequestURI());
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Method method = handlerMethod.getMethod();
			LoginRequired loginRequired = method.getAnnotation(LoginRequired.class);
			if (loginRequired != null && hostHolder.getUser() == null) {
				logger.info("用户未登录，即将跳转到登录页面: {}", CommunityUtil.contextPathJudge(request.getContextPath()) + "/login");
				response.sendRedirect(CommunityUtil.contextPathJudge(request.getContextPath()) + "/login");
				return false;
			}
		}
		return true;
	}
}
