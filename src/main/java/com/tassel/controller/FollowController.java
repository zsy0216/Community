package com.tassel.controller;

import com.tassel.entity.User;
import com.tassel.service.FollowService;
import com.tassel.util.CommunityUtil;
import com.tassel.util.HostHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author shuaiyin.zhang
 * @description
 * @date 2020/09/12
 */
@Controller
@ResponseBody
public class FollowController {

	@Resource
	FollowService followService;

	@Resource
	HostHolder hostHolder;

	@PostMapping("/follow")
	public String follow(int entityType, int entityId) {
		User user = hostHolder.getUser();
		followService.follow(user.getId(), entityType, entityId);
		return CommunityUtil.getJSONString(0, "已关注");
	}

	@PostMapping("/unFollow")
	public String unFollow(int entityType, int entityId) {
		User user = hostHolder.getUser();
		followService.unFollow(user.getId(), entityType, entityId);
		return CommunityUtil.getJSONString(0, "已取消关注");
	}

}
