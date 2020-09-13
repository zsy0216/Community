package com.tassel.event;

import com.alibaba.fastjson.JSONObject;
import com.tassel.entity.Event;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author shuaiyin.zhang
 * @description
 * @date 2020/09/13
 */
@Component
public class EventProducer {

	@Resource
	KafkaTemplate kafkaTemplate;

	/**
	 * 处理事件
	 * @param event
	 */
	public void fireEvent(Event event) {
		// 将事件发布到指定的主题
		kafkaTemplate.send(event.getTopic(), JSONObject.toJSONString(event));
	}
}
