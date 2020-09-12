package com.tassel.controller;

import com.tassel.entity.User;
import com.tassel.service.FollowService;
import com.tassel.service.UserService;
import com.tassel.util.CommunityConstant;
import com.tassel.util.CommunityUtil;
import com.tassel.util.HostHolder;
import com.tassel.util.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author shuaiyin.zhang
 * @description
 * @date 2020/09/12
 */
@Controller
public class FollowController implements CommunityConstant {

	@Resource
	FollowService followService;

	@Resource
	HostHolder hostHolder;

	@Resource
	UserService userService;

	@PostMapping("/follow")
	@ResponseBody
	public String follow(int entityType, int entityId) {
		User user = hostHolder.getUser();
		followService.follow(user.getId(), entityType, entityId);
		return CommunityUtil.getJSONString(0, "已关注");
	}

	@PostMapping("/unFollow")
	@ResponseBody
	public String unFollow(int entityType, int entityId) {
		User user = hostHolder.getUser();
		followService.unFollow(user.getId(), entityType, entityId);
		return CommunityUtil.getJSONString(0, "已取消关注");
	}

	@GetMapping("/followees/{userId}")
	public String getFollowees(@PathVariable("userId") int userId, Page page, Model model) {
		User user = userService.queryUserById(userId);
		if (user == null) {
			throw new RuntimeException("用户不存在");
		}
		model.addAttribute("user", user);

		page.setLimit(5);
		page.setPath("/followees/" + userId);
		page.setRows((int) followService.findFolloweeCount(userId, ENTITY_TYPE_USER));

		List<Map<String, Object>> userList = followService.findFolloweeList(userId, page.getOffset(), page.getLimit());
		if (userList != null) {
			for (Map<String, Object> map : userList) {
				User u = (User) map.get("user");
				map.put("hasFollowed", hasFollowed(u.getId()));
			}
		}
		model.addAttribute("users", userList);

		return "/site/followee";
	}

	@GetMapping("/followers/{userId}")
	public String getFollowers(@PathVariable("userId") int userId, Page page, Model model) {
		User user = userService.queryUserById(userId);
		if (user == null) {
			throw new RuntimeException("用户不存在");
		}
		model.addAttribute("user", user);

		page.setLimit(5);
		page.setPath("/followers/" + userId);
		page.setRows((int) followService.findFollowerCount(ENTITY_TYPE_USER, userId));

		List<Map<String, Object>> userList = followService.findFollowerList(userId, page.getOffset(), page.getLimit());
		if (userList != null) {
			for (Map<String, Object> map : userList) {
				User u = (User) map.get("user");
				map.put("hasFollowed", hasFollowed(u.getId()));
			}
		}
		model.addAttribute("users", userList);

		return "/site/follower";
	}

	private boolean hasFollowed(int userId) {
		if (hostHolder.getUser() == null) {
			return false;
		}
		return followService.hasFollowed(hostHolder.getUser().getId(), ENTITY_TYPE_USER, userId);
	}

}
