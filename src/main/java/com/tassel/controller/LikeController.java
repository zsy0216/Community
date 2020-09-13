package com.tassel.controller;

import com.tassel.entity.Event;
import com.tassel.entity.User;
import com.tassel.event.EventProducer;
import com.tassel.service.LikeService;
import com.tassel.util.CommunityConstant;
import com.tassel.util.CommunityUtil;
import com.tassel.util.HostHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shuaiyin.zhang
 * @description
 * @date 2020/09/11
 */
@Controller
public class LikeController implements CommunityConstant {

	@Resource
	LikeService likeService;

	@Resource
	HostHolder hostHolder;

	@Resource
	EventProducer eventProducer;

	@PostMapping("/like")
	@ResponseBody
	public String like(int entityType, int entityId, int entityUserId, int postId) {
		User user = hostHolder.getUser();

		// 点赞
		likeService.like(user.getId(), entityType, entityId, entityUserId);
		// 数量
		long likeCount = likeService.findEntityLikeCount(entityType, entityId);
		// 状态
		int likeStatus = likeService.findEntityLikeStatus(user.getId(), entityType, entityId);
		// 返回结果
		Map<String, Object> map = new HashMap<>();
		map.put("likeCount", likeCount);
		map.put("likeStatus", likeStatus);

		// 触发点赞事件
		if (likeStatus == 1) {
			Event event = new Event().setTopic(TOPIC_LIKE).setUserId(hostHolder.getUser().getId()).setEntityType(entityType).setEntityId(entityId).setEntityUserId(entityUserId).setData("postId", postId);
			eventProducer.fireEvent(event);
		}

		return CommunityUtil.getJSONString(0, null, map);
	}
}
