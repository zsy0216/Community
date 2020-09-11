package com.tassel.service;

/**
 * @author shuaiyin.zhang
 * @description
 * @date 2020/09/11
 */
public interface LikeService {

	/**
	 * 点赞：存入 redis 根据 userId 判断该用户是否对 entity 点赞
	 *
	 * @param userId
	 * @param entityType
	 * @param entityId
	 * @param entityUserId
	 */
	void like(int userId, int entityType, int entityId, int entityUserId);

	/**
	 * 查询某实体点赞的数量
	 *
	 * @param entityType
	 * @param entityId
	 * @return
	 */
	long findEntityLikeCount(int entityType, int entityId);

	/**
	 * 查询某人对某实体的点赞状态
	 *
	 * @param userId
	 * @param entityType
	 * @param entityId
	 * @return
	 */
	int findEntityLikeStatus(int userId, int entityType, int entityId);

	/**
	 * 查询某个用户获得赞的数量
	 *
	 * @param userId
	 * @return
	 */
	int findUserLikeCount(int userId);
}
