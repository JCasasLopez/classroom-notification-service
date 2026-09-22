package dev.jcasaslopez.notification.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
	
	@Value("${kafka.topic.name.notifications}") private String topicName;
	@Value("${kafka.topic.replicas}") private  int numberReplicas;
	@Value("${kafka.topic.min-in-sync-replicas}") private  int numberInSyncReplicas;
	@Value("${kafka.topic.partitions}") private  int numberPartitions;

	private static final Logger logger = LoggerFactory.getLogger(KafkaTopicConfig.class);
	
	@Bean
	NewTopic createNotificationsTopic() {
		logger.info("Configuring Kafka Topic: {} with {} partitions and {} replicas", topicName, numberPartitions, numberReplicas);
		return TopicBuilder.name(topicName)
				.partitions(numberPartitions)
				.replicas(numberReplicas) 
				.config(TopicConfig.MIN_IN_SYNC_REPLICAS_CONFIG, String.valueOf(numberInSyncReplicas))
				
				// Short 24h retention as notification events are ephemeral and immediately consumed
				.config(TopicConfig.RETENTION_MS_CONFIG, String.valueOf(24 * 60 * 60 * 1000L)) 
				
				// Caps topic disk usage at 100 MB to prevent unexpected disk saturation
				.config(TopicConfig.RETENTION_BYTES_CONFIG, String.valueOf(10 * 1024 * 1024L))
				
				// Relatively small 10 MB segments force timely log rolls so retention policies actually trigger on low volume
				.config(TopicConfig.SEGMENT_BYTES_CONFIG, String.valueOf(1 * 1024 * 1024L)) 
				.build();
	}

}
