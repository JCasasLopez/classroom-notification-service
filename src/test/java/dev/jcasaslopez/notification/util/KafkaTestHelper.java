package dev.jcasaslopez.notification.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import dev.jcasaslopez.classroom.shared.event.NotificationEvent;
import dev.jcasaslopez.notification.dto.Email;

public class KafkaTestHelper {
	
	public static KafkaConsumer<String, Email> createNotificationConsumer(String bootstrapServers, String topic) {
	    Map<String, Object> props = new HashMap<>();
	    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
	    props.put(ConsumerConfig.GROUP_ID_CONFIG, "test-notifications-" + UUID.randomUUID());
	    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
	    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
	    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getName());
	    props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
	    props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, NotificationEvent.class.getName());
	    props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);

	    KafkaConsumer<String, Email> consumer = new KafkaConsumer<>(props);

	    TopicPartition topicPartition = new TopicPartition(topic, 0);
	    consumer.assign(Collections.singletonList(topicPartition));
	  	consumer.seekToEnd(Collections.singletonList(topicPartition));

	    return consumer;
	}

}
