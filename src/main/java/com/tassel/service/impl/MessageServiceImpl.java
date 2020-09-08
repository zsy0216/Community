package com.tassel.service.impl;

import com.tassel.entity.Message;
import com.tassel.mapper.MessageMapper;
import com.tassel.service.MessageService;
import org.springframework.stereotype.Service;

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
}
