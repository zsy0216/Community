package com.tassel.service.impl;

import com.tassel.entity.Message;
import com.tassel.mapper.MessageMapper;
import com.tassel.service.MessageService;
import com.tassel.util.SensitiveFilter;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author shuaiyin.zhang
 * @description
 * @date 2020/09/08
 */
@Service
public class MessageServiceImpl implements MessageService {

	@Resource
	MessageMapper messageMapper;

	@Resource
	SensitiveFilter sensitiveFilter;

	@Override
	public List<Message> selectConversations(int userId, int offset, int limit) {
		return messageMapper.selectConversations(userId, offset, limit);
	}

	@Override
	public Integer selectConversationCount(int userId) {
		return messageMapper.selectConversationCount(userId);
	}

	@Override
	public List<Message> selectLetters(String conversationId, int offset, int limit) {
		return messageMapper.selectLetters(conversationId, offset, limit);
	}

	@Override
	public Integer selectLetterCount(String conversationId) {
		return messageMapper.selectLetterCount(conversationId);
	}

	@Override
	public Integer selectLetterUnreadCount(int userId, String conversationId) {
		return messageMapper.selectLetterUnreadCount(userId, conversationId);
	}

	@Override
	public Integer insertMessage(Message message) {
		message.setContent(HtmlUtils.htmlEscape(message.getContent()));
		message.setContent(sensitiveFilter.filter(message.getContent()));
		return messageMapper.insertMessage(message);
	}

	@Override
	public Integer readMessage(List<Integer> ids) {
		return messageMapper.updateStatus(ids, 1);
	}

	@Override
	public Message selectLatestNotice(int userId, String topic) {
		return messageMapper.selectLatestNotice(userId, topic);
	}

	@Override
	public Integer selectNoticeCount(int userId, String topic) {
		return messageMapper.selectNoticeCount(userId, topic);
	}

	@Override
	public Integer selectNoticeUnreadCount(int userId, String topic) {
		return messageMapper.selectNoticeUnreadCount(userId, topic);
	}

	@Override
	public List<Message> selectNotices(int userId, String topic, int offset, int limit) {
		return messageMapper.selectNotices(userId, topic, offset, limit);
	}
}
