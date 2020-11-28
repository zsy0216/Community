package com.tassel.controller;

import com.google.code.kaptcha.Producer;
import com.tassel.entity.User;
import com.tassel.service.UserService;
import com.tassel.util.CommunityConstant;
import com.tassel.util.CommunityUtil;
import com.tassel.util.RedisKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Ep流苏
 * @Date: 2020/6/14 16:17
 * @Description:
 */
@Controller
public class LoginController implements CommunityConstant {

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Resource
	UserService userService;

	@Resource
	Producer kaptchaProducer;

	@Value("${server.servlet.context-path}")
	private String contextPath;

	@Resource
	RedisTemplate redisTemplate;

	@GetMapping("/register")
	public String toRegisterPage() {
		return "site/register";
	}

	@GetMapping("/login")
	public String toLoginPage() {
		return "site/login";
	}

	/**
	 * 注册
	 *
	 * @param model
	 * @param user
	 * @return
	 */
	@PostMapping("/register")
	public String register(Model model, User user) {
		Map<String, Object> map = userService.register(user);
		if (map == null || map.isEmpty()) {
			model.addAttribute("msg", "注册成功，已经向您的邮箱发送了一封激活邮件，请尽快激活!");
			model.addAttribute("target", "/index");
			logger.info("邮件发送成功，需要查看请配置自己的邮箱信息，或与我联系：qq 594983498");
			return "/site/operate-result";
		} else {
			model.addAttribute("usernameMsg", map.get("usernameMsg"));
			model.addAttribute("passwordMsg", map.get("passwordMsg"));
			model.addAttribute("emailMsg", map.get("emailMsg"));
			return "site/register";
		}
	}

	/**
	 * 账号激活
	 * http://localhost:8023/activation/101/code
	 *
	 * @param model
	 * @param userId
	 * @param code
	 * @return
	 */
	@GetMapping("/activation/{userId}/{code}")
	public String activation(Model model, @PathVariable("userId") int userId, @PathVariable("code") String code) {
		int result = userService.activation(userId, code);
		switch (result) {
			case ACTIVATION_SUCCESS:
				model.addAttribute("msg", "激活成功,您的账号已经可以正常使用了!");
				model.addAttribute("target", "/login");
				logger.info("如需自己手动操作请配置自己的邮箱信息，或与我联系：qq 594983498");
				break;
			case ACTIVATION_REPEAT:
				model.addAttribute("msg", "重复操作,您的账号已经激活过了!");
				model.addAttribute("target", "/index");
				logger.info("如需自己手动操作请配置自己的邮箱信息，或与我联系：qq 594983498");
				break;
			default:
				model.addAttribute("msg", "激活失败,请检查您的激活码是否正确!");
				model.addAttribute("target", "/index");
				logger.info("如需自己手动操作请配置自己的邮箱信息，或与我联系：qq 594983498");
				break;
		}
		return "/site/operate-result";
	}

	/**
	 * 获取登录验证码图片
	 *
	 * @param response //* @param session
	 */
	@GetMapping("/kaptcha")
	public void getKaptcha(HttpServletResponse response/*, HttpSession session*/) {
		// 生成验证码
		String text = kaptchaProducer.createText();
		BufferedImage image = kaptchaProducer.createImage(text);

		// 将验证码存入 session
		//session.setAttribute("kaptcha", text);

		// 验证码的归属
		String kaptchaOwner = CommunityUtil.generateUUID();
		Cookie cookie = new Cookie("kaptchaOwner", kaptchaOwner);
		cookie.setMaxAge(60);
		cookie.setPath(contextPath);
		response.addCookie(cookie);
		// 将验证码存入 redis
		String redisKey = RedisKeyUtil.getKaptchaKey(kaptchaOwner);
		redisTemplate.opsForValue().set(redisKey, text, 60, TimeUnit.SECONDS);

		// 将图片输出给浏览器
		response.setContentType("image/png");
		ServletOutputStream os = null;
		try {
			os = response.getOutputStream();
			ImageIO.write(image, "png", os);
		} catch (IOException e) {
			logger.error("响应验证码失败" + e.getMessage());
		}
	}

	/**
	 * 登录逻辑
	 * 对于 普通参数类型，不会自动封装到 Model 对象中，所以模板引擎中无法取到，
	 * 可以使用 request 域对象取值
	 *
	 * @param username
	 * @param password
	 * @param code
	 * @param rememberMe
	 * @param model      //* @param session
	 * @param response
	 * @return
	 */
	@PostMapping("/login")
	public String login(String username, String password, String code, boolean rememberMe, Model model/*, HttpSession session*/, HttpServletResponse response, @CookieValue("kaptchaOwner") String kaptchaOwner) {
		// 检查验证码
		//String kaptcha = (String) session.getAttribute("kaptcha");
		String kaptcha = null;
		if (StringUtils.isNotBlank(kaptchaOwner)) {
			String redisKey = RedisKeyUtil.getKaptchaKey(kaptchaOwner);
			kaptcha = (String) redisTemplate.opsForValue().get(redisKey);
		}
		if (StringUtils.isAnyBlank(kaptcha, code) || !code.equalsIgnoreCase(kaptcha)) {
			model.addAttribute("codeMsg", "验证码不正确!");
			return "/site/login";
		}

		//检查账号、密码、失效时间 判断登录
		int expiredSeconds = rememberMe ? REMEMBER_EXPIRED_SECONDS : DEFAULT_EXPIRED_SECONDS;
		Map<String, Object> map = userService.login(username, password, expiredSeconds);
		// 登录成功
		if (map.containsKey("ticket")) {
			Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
			cookie.setPath(contextPath);
			cookie.setMaxAge(expiredSeconds);
			response.addCookie(cookie);
			return "redirect:/index";
		} else { // 登录失败
			model.addAttribute("usernameMsg", map.get("usernameMsg"));
			model.addAttribute("passwordMsg", map.get("passwordMsg"));
			return "/site/login";
		}
	}

	@GetMapping("/logout")
	public String logout(@CookieValue("ticket") String ticket) {
		userService.logout(ticket);
		// 默认 get 请求路径
		return "redirect:/login";
	}
}
