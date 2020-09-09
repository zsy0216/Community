package com.tassel.controller;

import com.tassel.entity.Message;
import com.tassel.entity.User;
import com.tassel.service.MessageService;
import com.tassel.service.UserService;
import com.tassel.util.CommunityUtil;
import com.tassel.util.HostHolder;
import com.tassel.util.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author shuaiyin.zhang
 * @description
 * @date 2020/09/08
 */
@Controller
public class MessageController {

	@Resource
	MessageService messageService;

	@Resource
	HostHolder hostHolder;

	@Resource
	UserService userService;

	/**
	 * 私信列表
	 *
	 * @param model
	 * @param page
	 * @return
	 */
	@GetMapping("/letter/list")
	public String getLetterList(Model model, Page page) {
		Integer.valueOf("abc");
		User user = hostHolder.getUser();
		// 设置分页信息
		page.setLimit(5);
		page.setPath("/letter/list");
		page.setRows(messageService.selectConversationCount(user.getId()));

		// 会话列表
		List<Message> conversationList = messageService.selectConversations(user.getId(), page.getOffset(), page.getLimit());
		List<Map<String, Object>> conversations = new ArrayList<>();
		if (conversationList != null) {
			for (Message message : conversationList) {
				Map<String, Object> map = new HashMap<>();
				map.put("conversation", message);
				map.put("letterCount", messageService.selectLetterCount(message.getConversationId()));
				map.put("unreadCount", messageService.selectLetterUnreadCount(user.getId(), message.getConversationId()));
				int targetId = user.getId().equals(message.getFromId()) ? message.getToId() : message.getFromId();
				map.put("target", userService.queryUserById(targetId));
				conversations.add(map);
			}
		}
		model.addAttribute("conversations", conversations);

		// 查询未读消息数量
		Integer letterUnreadCount = messageService.selectLetterUnreadCount(user.getId(), null);
		model.addAttribute("letterUnreadCount", letterUnreadCount);

		return "/site/letter";
	}

	@GetMapping("/letter/detail/{conversationId}")
	public String getLetterDetail(@PathVariable("conversationId") String conversationId, Page page, Model model) {
		// 分页信息
		page.setLimit(5);
		page.setPath("/letter/detail/" + conversationId);
		page.setRows(messageService.selectLetterCount(conversationId));

		// 私信列表
		List<Message> letterList = messageService.selectLetters(conversationId, page.getOffset(), page.getLimit());
		List<Map<String, Object>> letters = new ArrayList<>();
		if (letterList != null) {
			for (Message message : letterList) {
				Map<String, Object> map = new HashMap<>();
				map.put("letter", message);
				map.put("fromUser", userService.queryUserById(message.getFromId()));
				letters.add(map);
			}
		}
		model.addAttribute("letters", letters);

		// 私信目标
		model.addAttribute("target", getLetterTarget(conversationId));

		//设置消息为已读
		List<Integer> ids = getLetterIds(letterList);
		if (!ids.isEmpty()) {
			messageService.readMessage(ids);
		}

		return "/site/letter-detail";
	}

	private List<Integer> getLetterIds(List<Message> letterList) {
		List<Integer> ids = new ArrayList<>();

		if (letterList != null) {
			for (Message message : letterList) {
				if (hostHolder.getUser().getId().equals(message.getToId()) && message.getStatus().equals(0)) {
					ids.add(message.getId());
				}
			}
		}

		return ids;
	}

	private User getLetterTarget(String conversationId) {
		String[] ids = conversationId.split("_");
		User user0 = userService.queryUserById(Integer.parseInt(ids[0]));
		User user1 = userService.queryUserById(Integer.parseInt(ids[1]));
		return hostHolder.getUser().equals(user0) ? user1 : user0;
	}

	@PostMapping("/letter/send")
	@ResponseBody
	public String sentLetter(String toName, String content) {
		User target = userService.queryUserByName(toName);
		if (ObjectUtils.isEmpty(target)) {
			return CommunityUtil.getJSONString(1, "目标用户不存在!");
		}

		Message message = new Message();
		message.setFromId(hostHolder.getUser().getId());
		message.setToId(target.getId());
		if (message.getFromId() < message.getToId()) {
			message.setConversationId(message.getFromId() + "_" + message.getToId());
		} else {
			message.setConversationId(message.getToId() + "_" + message.getFromId());
		}
		message.setContent(content);
		message.setStatus(0);
		message.setCreateTime(new Date());
		messageService.insertMessage(message);

		return CommunityUtil.getJSONString(0);
	}
}
