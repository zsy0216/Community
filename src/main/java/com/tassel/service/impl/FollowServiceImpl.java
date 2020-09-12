package com.tassel.service.impl;

import com.tassel.service.FollowService;
import com.tassel.util.RedisKeyUtil;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author shuaiyin.zhang
 * @description
 * @date 2020/09/12
 */
@Service
public class FollowServiceImpl implements FollowService {

	@Resource
	RedisTemplate<String, Object> redisTemplate;

	@Override
	public void follow(int userId, int entityType, int entityId) {
		// 一个方法有两次操作请求，加入事务
		redisTemplate.execute(new SessionCallback<Object>() {
			@Override
			public Object execute(RedisOperations operations) throws DataAccessException {
				String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
				String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);

				operations.multi();

				operations.opsForZSet().add(followeeKey, entityId, System.currentTimeMillis());
				operations.opsForZSet().add(followerKey, userId, System.currentTimeMillis());

				return operations.exec();
			}
		});
	}

	@Override
	public void unFollow(int userId, int entityType, int entityId) {
		// 一个方法有两次操作请求，加入事务
		redisTemplate.execute(new SessionCallback<Object>() {
			@Override
			public Object execute(RedisOperations operations) throws DataAccessException {
				String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
				String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);

				operations.multi();

				operations.opsForZSet().remove(followeeKey, entityId);
				operations.opsForZSet().remove(followerKey, userId);

				return operations.exec();
			}
		});
	}

	@Override
	public long findFolloweeCount(int userId, int entityType) {
		String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
		return redisTemplate.opsForZSet().zCard(followeeKey);
	}

	@Override
	public long findFollowerCount(int entityType, int entityId) {
		String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
		return redisTemplate.opsForZSet().zCard(followerKey);
	}

	@Override
	public boolean hasFollowed(int userId, int entityType, int entityId) {
		String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
		return redisTemplate.opsForZSet().score(followeeKey, entityId) != null;
	}
}
