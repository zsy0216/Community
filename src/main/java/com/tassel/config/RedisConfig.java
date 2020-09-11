package com.tassel.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @author shuaiyin.zhang
 * @description
 * @date 2020/09/11
 */
@Configuration
public class RedisConfig {

	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, Object> template = new RedisTemplate();
		template.setConnectionFactory(redisConnectionFactory);

		// 设置 key 的序列化方式
		template.setKeySerializer(RedisSerializer.string());
		// 设置普通 value 的序列化方式
		template.setValueSerializer(RedisSerializer.json());
		// 设置 hash key 的序列化方式
		template.setHashKeySerializer(RedisSerializer.string());
		// 设置 hash value 的序列化方式
		template.setHashValueSerializer(RedisSerializer.json());

		template.afterPropertiesSet();
		return template;
	}
}
