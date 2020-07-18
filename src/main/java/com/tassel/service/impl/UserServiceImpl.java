package com.tassel.service.impl;

import com.tassel.entity.LoginTicket;
import com.tassel.entity.User;
import com.tassel.mapper.LoginTicketMapper;
import com.tassel.mapper.UserMapper;
import com.tassel.service.UserService;
import com.tassel.util.CommunityConstant;
import com.tassel.util.CommunityUtil;
import com.tassel.util.MailClient;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Ep流苏
 * @Date: 2020/6/14 10:37
 * @Description:
 */
@Service
public class UserServiceImpl implements UserService, CommunityConstant {
	@Resource
	UserMapper userMapper;
	@Resource
	MailClient mailClient;
	@Resource
	TemplateEngine templateEngine;
	@Resource
	LoginTicketMapper loginTicketMapper;
	@Value("${community.path.domain}")
	private String domain;
	@Value("${server.servlet.context-path}")
	private String contextPath;

	@Override
	public User queryUserById(int id) {
		return userMapper.queryUserById(id);
	}

	@Override
	public Map<String, Object> register(User user) {
		Map<String, Object> map = new HashMap<>();
		// 空值处理
		if (user == null) {
			throw new IllegalArgumentException("参数不能为空!");
		}
		if (StringUtils.isBlank(user.getUsername())) {
			map.put("usernameMsg", "账号不能为空!");
			return map;
		}
		if (StringUtils.isBlank(user.getPassword())) {
			map.put("passwordMsg", "密码不能为空!");
			return map;
		}
		if (StringUtils.isBlank(user.getEmail())) {
			map.put("emailMsg", "邮箱不能为空!");
			return map;
		}

		// 验证账号
		User u = userMapper.queryByName(user.getUsername());
		if (u != null) {
			map.put("usernameMsg", "该账号已存在!");
			return map;
		}
		// 验证邮箱
		u = userMapper.queryByEmail(user.getEmail());
		if (u != null) {
			map.put("emailMsg", "该邮箱已被注册!");
			return map;
		}

		// 注册用户
		user.setSalt(CommunityUtil.generateUUID().substring(0, 5));
		user.setPassword(CommunityUtil.md5(user.getPassword() + user.getSalt()));
		user.setType(0);
		user.setStatus(0);
		user.setActivationCode(CommunityUtil.generateUUID());
		user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
		user.setCreateTime(new Date());
		userMapper.insertUser(user);

		// 激活邮件
		Context context = new Context();
		context.setVariable("email", user.getEmail());
		// http://localhost:8023/activation/101/code
		//String url = domain + ("/".equals(contextPath) ? "" : contextPath) + "/activation/" + user.getId() + "/" + user.getActivationCode();
		String url = domain + CommunityUtil.contextPathJudge(contextPath) + "/activation/" + user.getId() + "/" + user.getActivationCode();
		context.setVariable("url", url);
		String content = templateEngine.process("/mail/activation", context);
		mailClient.sendMail(user.getEmail(), "激活账号", content);

		return map;
	}

	@Override
	public Integer activation(int userId, String code) {
		User user = userMapper.queryUserById(userId);
		if (user.getStatus() == 1) {
			return ACTIVATION_REPEAT;
		} else if (user.getActivationCode().equals(code)) {
			userMapper.updateStatus(1, userId);
			return ACTIVATION_SUCCESS;
		} else {
			return ACTIVATION_FAILURE;
		}
	}

	@Override
	public Map<String, Object> login(String username, String password, int expiredSecond) {
		Map<String, Object> map = new HashMap<>(0);

		// 空值处理
		if (StringUtils.isBlank(username)) {
			map.put("usernameMsg", "用户名不能为空!");
			return map;
		}
		if (StringUtils.isBlank(password)) {
			map.put("passwordMsg", "密码不能为空!");
			return map;
		}

		// 验证账号
		User user = userMapper.queryByName(username);
		if (ObjectUtils.isEmpty(user)) {
			map.put("usernameMsg", "该账号不存在!");
			return map;
		}

		// 验证激活状态
		if (user.getStatus() == 0) {
			map.put("usernameMsg", "该账号未激活!");
			return map;
		}

		// 验证密码
		password = CommunityUtil.md5(password + user.getSalt());
		if (!user.getPassword().equals(password)) {
			map.put("passwordMsg", "密码不正确!");
			return map;
		}

		// 成功登录, 生成登录凭证
		LoginTicket loginTicket = new LoginTicket();
		loginTicket.setUserId(user.getId());
		loginTicket.setStatus(0);
		loginTicket.setTicket(CommunityUtil.generateUUID());
		loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSecond * 1000));
		loginTicketMapper.insertLoginTicket(loginTicket);

		map.put("ticket", loginTicket.getTicket());
		return map;
	}

	@Override
	public void logout(String ticket) {
		loginTicketMapper.updateStatus(ticket, 1);
	}

	@Override
	public LoginTicket findLoginTicket(String ticket) {
		return loginTicketMapper.selectByTicket(ticket);
	}

	@Override
	public int updateHeader(Integer userId, String headerUrl) {
		return userMapper.updateHeader(headerUrl, userId);
	}
}
