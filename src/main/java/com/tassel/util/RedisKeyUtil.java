package com.tassel.util;

/**
 * @author shuaiyin.zhang
 * @description
 * @date 2020/09/11
 */
public class RedisKeyUtil {

	private static final String SPLIT = ":";
	private static final String PREFIX_ENTITY_LIKE = "like:entity";

	/**
	 * 某个实体的赞
	 * like:entity:entityType:entityId -> set(userId)
	 *
	 * @param entityType
	 * @param entityId
	 * @return
	 */
	public static String getEntityLikeKey(int entityType, int entityId) {
		return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
	}
}
