package com.tassel;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author shuaiyin.zhang
 * @description
 * @date 2020/09/11
 */
@SpringBootTest
public class RedisTest {

	@Resource
	RedisTemplate redisTemplate;

	@Test
	public void testStrings() {
		String redisKey = "test:count";

		redisTemplate.opsForValue().set(redisKey, 1);
		System.out.println(redisTemplate.opsForValue().get(redisKey));
		System.out.println(redisTemplate.opsForValue().increment(redisKey));
		System.out.println(redisTemplate.opsForValue().decrement(redisKey));
	}

	@Test
	public void testHashes() {
		String redisKey = "test:user";

		redisTemplate.opsForHash().put(redisKey, "id", 1);
		redisTemplate.opsForHash().put(redisKey, "username", "zhangsan");
		;
		System.out.println(redisTemplate.opsForHash().get(redisKey, "id"));
		;
		System.out.println(redisTemplate.opsForHash().get(redisKey, "username"));
		;
	}

	@Test
	public void testLists() {
		String redisKey = "test:ids";

		redisTemplate.opsForList().leftPush(redisKey, 101);
		redisTemplate.opsForList().leftPush(redisKey, 102);
		redisTemplate.opsForList().leftPush(redisKey, 103);

		System.out.println(redisTemplate.opsForList().size(redisKey));
		System.out.println(redisTemplate.opsForList().index(redisKey, 0));
		System.out.println(redisTemplate.opsForList().range(redisKey, 0, 2));

		System.out.println(redisTemplate.opsForList().leftPop(redisKey));
		System.out.println(redisTemplate.opsForList().leftPop(redisKey));
		System.out.println(redisTemplate.opsForList().leftPop(redisKey));
	}

	@Test
	public void testSets() {
		String redisKey = "test:teachers";

		redisTemplate.opsForSet().add(redisKey, "刘备", "关羽", "张飞", "赵云", "诸葛亮");

		System.out.println(redisTemplate.opsForSet().size(redisKey));
		System.out.println(redisTemplate.opsForSet().pop(redisKey));
		System.out.println(redisTemplate.opsForSet().members(redisKey));
	}

	@Test
	public void testSortedSets() {
		String redisKey = "test:students";

		redisTemplate.opsForZSet().add(redisKey, "唐僧", 80);
		redisTemplate.opsForZSet().add(redisKey, "悟空", 90);
		redisTemplate.opsForZSet().add(redisKey, "八戒", 50);
		redisTemplate.opsForZSet().add(redisKey, "沙僧", 70);
		redisTemplate.opsForZSet().add(redisKey, "白龙马", 60);

		System.out.println(redisTemplate.opsForZSet().zCard(redisKey));
		System.out.println(redisTemplate.opsForZSet().score(redisKey, "八戒"));
		System.out.println(redisTemplate.opsForZSet().reverseRank(redisKey, "八戒"));
		System.out.println(redisTemplate.opsForZSet().reverseRange(redisKey, 0, 2));
	}

	@Test
	public void testKeys() {
		redisTemplate.delete("test:user");

		System.out.println(redisTemplate.hasKey("test:user"));

		redisTemplate.expire("test:students", 10, TimeUnit.SECONDS);
	}

	@Test
	public void testBoundOperations() {
		String redisKey = "test:count";
		BoundValueOperations operations = redisTemplate.boundValueOps(redisKey);
		operations.increment();
	}
}
