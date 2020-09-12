package com.tassel.service.impl;

import com.tassel.entity.User;
import com.tassel.service.FollowService;
import com.tassel.service.UserService;
import com.tassel.util.CommunityConstant;
import com.tassel.util.RedisKeyUtil;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author shuaiyin.zhang
 * @description
 * @date 2020/09/12
 */
@Service
public class FollowServiceImpl implements FollowService, CommunityConstant {

	@Resource
	RedisTemplate redisTemplate;

	@Resource
	UserService userService;

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

	@Override
	public List<Map<String, Object>> findFolloweeList(int userId, int offset, int limit) {
		String followeeKey = RedisKeyUtil.getFolloweeKey(userId, ENTITY_TYPE_USER);
		Set<Integer> targetIds = redisTemplate.opsForZSet().reverseRange(followeeKey, offset, offset + limit - 1);

		if (targetIds == null) {
			return null;
		}

		List<Map<String, Object>> list = new ArrayList<>();
		for (Integer targetId : targetIds) {
			Map<String, Object> map = new HashMap<>();
			User user = userService.queryUserById(targetId);
			map.put("user", user);
			Double score = redisTemplate.opsForZSet().score(followeeKey, targetId);
			map.put("followTime", new Date(score.longValue()));
			list.add(map);
		}
		return list;
	}

	@Override
	public List<Map<String, Object>> findFollowerList(int userId, int offset, int limit) {
		String followerKey = RedisKeyUtil.getFollowerKey(ENTITY_TYPE_USER, userId);
		Set<Integer> targetIds = redisTemplate.opsForZSet().reverseRange(followerKey, offset, offset + limit - 1);

		if (targetIds == null) {
			return null;
		}

		List<Map<String, Object>> list = new ArrayList<>();
		for (Integer targetId : targetIds) {
			Map<String, Object> map = new HashMap<>();
			User user = userService.queryUserById(targetId);
			map.put("user", user);
			Double score = redisTemplate.opsForZSet().score(followerKey, targetId);
			map.put("followTime", new Date(score.longValue()));
			list.add(map);
		}
		return list;
	}
}
