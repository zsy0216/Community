package com.tassel.controller;

import com.tassel.annotation.LoginRequired;
import com.tassel.entity.User;
import com.tassel.service.FollowService;
import com.tassel.service.LikeService;
import com.tassel.service.UserService;
import com.tassel.util.CommunityConstant;
import com.tassel.util.CommunityUtil;
import com.tassel.util.HostHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author shuaiyin.zhang
 * @description
 * @date 2020/07/18
 */
@Controller
@RequestMapping("/user")
public class UserController implements CommunityConstant {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Value("${community.path.domain}")
	private String domain;
	@Value("${community.path.upload}")
	private String uploadPath;
	@Value("${server.servlet.context-path}")
	private String contextPath;

	@Resource
	UserService userService;
	@Resource
	HostHolder hostHolder;

	@Resource
	LikeService likeService;

	@Resource
	FollowService followService;

	@LoginRequired
	@GetMapping("/setting")
	public String toSettingPage() {
		logger.info("前往用户设置页面");
		return "/site/setting";
	}

	@LoginRequired
	@PostMapping("/upload")
	public String uploadHeader(MultipartFile headerImage, Model model) {
		if (headerImage == null) {
			logger.error("用户上传图片内容为空");
			model.addAttribute("error", "用户上传图片内容为空");
			return "/site/setting";
		}

		String fileName = headerImage.getOriginalFilename();
		String suffix = fileName.substring(fileName.lastIndexOf('.'));
		//String[] imageSuffix = new String[]{"png", "jpg", "bmp", "gif", "svg", "webp", "jpeg"};
		//List<String> imageList = Arrays.asList(imageSuffix);
		//if (StringUtils.isEmpty(suffix) || !imageList.contains(suffix)) {
		if (StringUtils.isEmpty(suffix)) {
			logger.error("图片文件格式不正确");
			model.addAttribute("error", "图片文件格式不正确");
			return "/site/setting";
		}

		// 生成随机文件名
		fileName = CommunityUtil.generateUUID() + suffix;
		// 路径
		File dest = new File(uploadPath + "/" + fileName);
		try {
			headerImage.transferTo(dest);
		} catch (IOException e) {
			logger.error("上传文件失败" + e.getMessage());
			throw new RuntimeException("上传文件失败, 服务器发生异常  " + e);
		}

		// 上传成功，更新当前用户的头像路径(web访问路径)
		// http://localhost:8023/user/header/xxx.png
		User user = hostHolder.getUser();
		String headerUrl = domain + CommunityUtil.contextPathJudge(contextPath) + "/user/header/" + fileName;
		userService.updateHeader(user.getId(), headerUrl);

		return "redirect:/index";
	}

	@GetMapping("/header/{fileName}")
	public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response) {
		// 文件存储路径
		fileName = uploadPath + "/" + fileName;
		// 文件后缀类型
		String suffix = fileName.substring(fileName.lastIndexOf("."));
		// 响应图片
		response.setContentType("image/" + suffix);
		try (OutputStream os = response.getOutputStream(); FileInputStream fis = new FileInputStream(fileName)) {
			byte[] buffer = new byte[1024];
			int b = 0;
			while ((b = fis.read(buffer)) != -1) {
				os.write(buffer, 0, b);
			}
		} catch (IOException e) {
			logger.error("读取头像文件失败: " + e.getMessage());
		}
	}

	/**
	 * 个人主页
	 *
	 * @param userId
	 * @param model
	 * @return
	 */
	@GetMapping("/profile/{userId}")
	public String getProfilePage(@PathVariable("userId") int userId, Model model) {
		User user = userService.queryUserById(userId);
		if (user == null) {
			throw new RuntimeException("该用户不存在");
		}

		// 用户
		model.addAttribute("user", user);
		// 获赞数量
		int likeCount = likeService.findUserLikeCount(userId);
		model.addAttribute("likeCount", likeCount);

		// 关注数量
		long followeeCount = followService.findFolloweeCount(userId, ENTITY_TYPE_USER);
		model.addAttribute("followeeCount", followeeCount);
		// 粉丝数量
		long followerCount = followService.findFollowerCount(ENTITY_TYPE_USER, userId);
		model.addAttribute("followerCount", followerCount);
		// 是否当前用户已关注此用户
		boolean hasFollowed = false;
		if (hostHolder.getUser() != null) {
			hasFollowed = followService.hasFollowed(hostHolder.getUser().getId(), ENTITY_TYPE_USER, userId);
		}
		model.addAttribute("hasFollowed", hasFollowed);

		return "/site/profile";
	}
}
