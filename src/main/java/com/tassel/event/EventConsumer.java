package com.tassel.event;

import com.alibaba.fastjson.JSONObject;
import com.tassel.entity.DiscussPost;
import com.tassel.entity.Event;
import com.tassel.entity.Message;
import com.tassel.service.DiscussPostService;
import com.tassel.service.ElasticsearchService;
import com.tassel.service.MessageService;
import com.tassel.util.CommunityConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shuaiyin.zhang
 * @description
 * @date 2020/09/13
 */
@Component
@Slf4j
public class EventConsumer implements CommunityConstant {

	@Resource
	MessageService messageService;

	@Resource
	DiscussPostService discussPostService;

	@Resource
	ElasticsearchService elasticsearchService;

	@KafkaListener(topics = {TOPIC_COMMENT, TOPIC_LIKE, TOPIC_FOLLOW})
	public void handleTopicMessage(ConsumerRecord record) {
		if (record == null || record.value() == null) {
			log.error("消息内容为空!");
		}

		Event event = JSONObject.parseObject(record.value().toString(), Event.class);
		if (event == null) {
			log.error("消息格式错误!");
			return;
		}

		// 发送站内通知
		Message message = new Message();
		message.setFromId(SYSTEM_USER_ID);
		message.setToId(event.getEntityUserId());
		message.setConversationId(event.getTopic());
		message.setStatus(0);
		message.setCreateTime(new Date());

		Map<String, Object> content = new HashMap<>();
		content.put("userId", event.getUserId());
		content.put("entityType", event.getEntityType());
		content.put("entityId", event.getEntityId());

		if (!event.getData().isEmpty()) {
			for (Map.Entry<String, Object> entry : event.getData().entrySet()) {
				content.put(entry.getKey(), entry.getValue());
			}
		}
		message.setContent(JSONObject.toJSONString(content));

		messageService.insertMessage(message);
	}

	/**
	 * 消费发帖事件
	 */
	@KafkaListener(topics = {TOPIC_PUBLISH})
	public void handlePublishMessage(ConsumerRecord record) {
		if (record == null || record.value() == null) {
			log.error("消息内容为空!");
		}

		Event event = JSONObject.parseObject(record.value().toString(), Event.class);
		if (event == null) {
			log.error("消息格式错误!");
			return;
		}

		DiscussPost post = discussPostService.selectDiscussPostById(event.getEntityId());
		elasticsearchService.saveDiscussPost(post);
	}
}
