package com.tassel.controller;

import com.tassel.entity.DiscussPost;
import com.tassel.entity.User;
import com.tassel.service.DiscussPostService;
import com.tassel.service.UserService;
import com.tassel.util.CommunityUtil;
import com.tassel.util.HostHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author shuaiyin.zhang
 * @description
 * @date 2020/09/07
 */
@Controller
@RequestMapping("/discuss")
public class DiscussPostController {

	@Resource
	DiscussPostService discussPostService;

	@Resource
	HostHolder hostHolder;

	@Resource
	UserService userService;

	@PostMapping("/insert")
	@ResponseBody
	public String insertDiscussPost(String title, String content) {
		User user = hostHolder.getUser();
		if (user == null) {
			return CommunityUtil.getJSONString(403, "你还没有登录!");
		}

		DiscussPost post = new DiscussPost();
		post.setUserId(user.getId());
		post.setTitle(title);
		post.setContent(content);
		post.setCreateTime(new Date());
		post.setStatus(0);
		post.setType(0);
		post.setCommentCount(0);
		post.setScore(0.0);
		discussPostService.insertDiscussPost(post);

		// 报错异常情况，后续统一处理
		return CommunityUtil.getJSONString(0, "帖子发布成功!");
	}

	@GetMapping("/detail/{discussPostId}")
	public String selectDiscussPostById(@PathVariable("discussPostId") Integer discussPostId, Model model) {
		// 帖子
		DiscussPost post = discussPostService.selectDiscussPostById(discussPostId);
		model.addAttribute("post", post);
		// 作者
		User user = userService.queryUserById(post.getUserId());
		model.addAttribute("user", user);
		// 回复等后续补充
		return "site/discuss-detail";
	}
}
