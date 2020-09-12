package com.tassel.service;

import java.util.List;
import java.util.Map;

/**
 * @author shuaiyin.zhang
 * @description
 * @date 2020/09/12
 */
public interface FollowService {

	/**
	 * 关注
	 *
	 * @param userId
	 * @param entityType
	 * @param entityId
	 */
	void follow(int userId, int entityType, int entityId);

	/**
	 * 取消关注
	 *
	 * @param userId
	 * @param entityType
	 * @param entityId
	 */
	void unFollow(int userId, int entityType, int entityId);

	/**
	 * 查询关注的实体的数量
	 *
	 * @param userId
	 * @param entityType
	 * @return
	 */
	long findFolloweeCount(int userId, int entityType);

	/**
	 * 查询实体的粉丝的数量
	 *
	 * @param entityType
	 * @param entityId
	 * @return
	 */
	long findFollowerCount(int entityType, int entityId);

	/**
	 * 查询当前用户是否已关注该实体
	 *
	 * @param userId
	 * @param entityType
	 * @param entityId
	 * @return
	 */
	boolean hasFollowed(int userId, int entityType, int entityId);

	/**
	 * 查询某用户关注的人
	 *
	 * @param userId
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<Map<String, Object>> findFolloweeList(int userId, int offset, int limit);

	/**
	 * 查询某用户的粉丝
	 *
	 * @param userId
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<Map<String, Object>> findFollowerList(int userId, int offset, int limit);

}
