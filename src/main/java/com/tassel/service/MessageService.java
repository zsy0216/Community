package com.tassel.service;

import com.tassel.entity.Message;

import java.util.List;

/**
 * @author shuaiyin.zhang
 * @description
 * @date 2020/09/08
 */
public interface MessageService {

	/**
	 * 查询当前用户的会话列表，针对每个会话只返回一条最新的私信。
	 *
	 * @param userId
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<Message> selectConversations(int userId, int offset, int limit);

	/**
	 * 查询当前用户的会话数量
	 *
	 * @param userId
	 * @return
	 */
	Integer selectConversationCount(int userId);

	/**
	 * 查询某个会话所包含的私信列表
	 *
	 * @param conversationId
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<Message> selectLetters(String conversationId, int offset, int limit);

	/**
	 * 查询某个会话所包含的私信数量
	 *
	 * @param conversationId
	 * @return
	 */
	Integer selectLetterCount(String conversationId);

	/**
	 * 查询未读私信数量
	 *
	 * @param userId
	 * @param conversationId
	 * @return
	 */
	Integer selectLetterUnreadCount(int userId, String conversationId);

	/**
	 * 新增消息
	 *
	 * @param message
	 * @return
	 */
	Integer insertMessage(Message message);

	/**
	 * 读取消息
	 *
	 * @param ids
	 * @return
	 */
	Integer readMessage(List<Integer> ids);

	/**
	 * 查询某个主题下最新的通知
	 *
	 * @param userId
	 * @param topic
	 * @return
	 */
	Message selectLatestNotice(int userId, String topic);

	/**
	 * 查询某个主题所包含的通知的数量
	 *
	 * @param userId
	 * @param topic
	 * @return
	 */
	Integer selectNoticeCount(int userId, String topic);

	/**
	 * 查询未读的通知的数量
	 *
	 * @param userId
	 * @param topic
	 * @return
	 */
	Integer selectNoticeUnreadCount(int userId, String topic);

	/**
	 * 查询某个主题所包含的通知列表
	 *
	 * @param userId
	 * @param topic
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<Message> selectNotices(int userId, String topic, int offset, int limit);
}
