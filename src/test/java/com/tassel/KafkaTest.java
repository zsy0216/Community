package com.tassel;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author shuaiyin.zhang
 * @description
 * @date 2020/09/13
 */
@SpringBootTest
public class KafkaTest {

	@Resource
	KafkaProducer kafkaProducer;

	@Test
	public void testKafka() {
		kafkaProducer.sentMessage("test", "hello");
		kafkaProducer.sentMessage("test", "world");
		try {
			Thread.sleep(1000 * 10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

@Component
class KafkaProducer {
	@Resource
	KafkaTemplate kafkaTemplate;

	public void sentMessage(String topic, String content) {
		kafkaTemplate.send(topic, content);
	}
}

@Component
class KafkaConsumer {
	@KafkaListener(topics = {"test"})
	public void handleMessage(ConsumerRecord record) {
		System.out.println(record.value());
	}
}