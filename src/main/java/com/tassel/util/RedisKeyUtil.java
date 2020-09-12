package com.tassel.util;

/**
 * @author shuaiyin.zhang
 * @description
 * @date 2020/09/11
 */
public class RedisKeyUtil {

	private static final String SPLIT = ":";

	/**
	 * 点赞 key 前缀
	 */
	private static final String PREFIX_ENTITY_LIKE = "like:entity";
	private static final String PREFIX_USER_LIKE = "like:user";

	/**
	 * 关注 key 前缀
	 */
	private static final String PREFIX_FOLLOWEE = "followee";
	private static final String PREFIX_FOLLOWER = "follower";

	/**
	 * 验证码前缀
	 */
	private static final String PREFIX_KAPTCHA = "kaptcha";

	/**
	 * 登录凭证
	 */
	private static final String PREFIX_TICKET = "ticket";

	/**
	 * 对用户做缓存
	 */
	private static final String PREFIX_USER = "user";

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

	/**
	 * 某个用户的赞
	 * like:user:user:Id -> int
	 *
	 * @param userId
	 * @return
	 */
	public static String getUserLikeKey(int userId) {
		return PREFIX_USER_LIKE + SPLIT + userId;
	}

	/**
	 * 某个用户关注的实体
	 * followee:userId:entityType ->  zset(entityId, now)
	 *
	 * @param userId
	 * @param entityType
	 * @return
	 */
	public static String getFolloweeKey(int userId, int entityType) {
		return PREFIX_FOLLOWEE + SPLIT + userId + SPLIT + entityType;
	}

	/**
	 * 某个实体拥有的粉丝
	 * follower:entityType:entityId -> zset(userId, now)
	 *
	 * @param entityType
	 * @param entityId
	 * @return
	 */
	public static String getFollowerKey(int entityType, int entityId) {
		return PREFIX_FOLLOWER + SPLIT + entityType + SPLIT + entityId;
	}

	/**
	 * 登录验证码
	 *
	 * @param owner
	 * @return
	 */
	public static String getKaptchaKey(String owner) {
		return PREFIX_KAPTCHA + SPLIT + owner;
	}

	/**
	 * 登录凭证
	 *
	 * @param ticket
	 * @return
	 */
	public static String getTicketKey(String ticket) {
		return PREFIX_TICKET + SPLIT + ticket;
	}

	/**
	 * 用户
	 *
	 * @param userId
	 * @return
	 */
	public static String getUserKey(int userId) {
		return PREFIX_USER + SPLIT + userId;
	}
}
