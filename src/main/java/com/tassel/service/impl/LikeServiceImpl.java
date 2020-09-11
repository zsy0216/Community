package com.tassel.service.impl;

import com.tassel.service.LikeService;
import com.tassel.util.RedisKeyUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author shuaiyin.zhang
 * @description
 * @date 2020/09/11
 */
@Service
public class LikeServiceImpl implements LikeService {

	@Resource
	RedisTemplate<String, Object> redisTemplate;

	@Override
	public void like(int userId, int entityType, int entityId) {
		String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
		Boolean isMember = redisTemplate.opsForSet().isMember(entityLikeKey, userId);
		if (isMember) {
			redisTemplate.opsForSet().remove(entityLikeKey, userId);
		} else {
			redisTemplate.opsForSet().add(entityLikeKey, userId);
		}
	}

	@Override
	public long findEntityLikeCount(int entityType, int entityId) {
		String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
		return redisTemplate.opsForSet().size(entityLikeKey);
	}

	@Override
	public int findEntityLikeStatus(int userId, int entityType, int entityId) {
		String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
		return redisTemplate.opsForSet().isMember(entityLikeKey, userId) ? 1 : 0;
	}
}
