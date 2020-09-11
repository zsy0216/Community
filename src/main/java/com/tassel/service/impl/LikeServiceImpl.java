package com.tassel.service.impl;

import com.tassel.service.LikeService;
import com.tassel.util.RedisKeyUtil;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.print.attribute.standard.CopiesSupported;

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
	public void like(int userId, int entityType, int entityId, int entityUserId) {
		// 加入事务
		redisTemplate.execute(new SessionCallback<Object>() {
			@Override
			public Object execute(RedisOperations operations) throws DataAccessException {
				String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
				String userLikeKey = RedisKeyUtil.getUserLikeKey(entityUserId);

				// 查询操作要在事务开启之前执行，否则在事务中不会立即执行。
				Boolean isMember = operations.opsForSet().isMember(entityLikeKey, userId);

				// 开启事务
				operations.multi();

				if (isMember) {
					operations.opsForSet().remove(entityLikeKey, userId);
					operations.opsForValue().decrement(userLikeKey);
				} else {
					operations.opsForSet().add(entityLikeKey, userId);
					operations.opsForValue().increment(userLikeKey);
				}

				// 执行
				return operations.exec();
			}
		});
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

	@Override
	public int findUserLikeCount(int userId) {
		String userLikeKey = RedisKeyUtil.getUserLikeKey(userId);
		Integer count = (Integer) redisTemplate.opsForValue().get(userLikeKey);
		return count == null ? 0 : count;
	}
}
