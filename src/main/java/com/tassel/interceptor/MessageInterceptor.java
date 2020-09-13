package com.tassel.interceptor;

import com.tassel.entity.User;
import com.tassel.service.MessageService;
import com.tassel.util.HostHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author shuaiyin.zhang
 * @description
 * @date 2020/09/13
 */
@Component
public class MessageInterceptor implements HandlerInterceptor {

	@Resource
	HostHolder hostHolder;

	@Resource
	MessageService messageService;

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		User user = hostHolder.getUser();
		if (user != null && modelAndView != null) {
			int letterUnreadCount = messageService.selectLetterUnreadCount(user.getId(), null);
			int noticeUnreadCount = messageService.selectNoticeUnreadCount(user.getId(), null);
			modelAndView.addObject("allUnreadCount", letterUnreadCount + noticeUnreadCount);		}
	}
}
