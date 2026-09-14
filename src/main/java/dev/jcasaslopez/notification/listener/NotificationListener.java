package dev.jcasaslopez.notification.listener;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import dev.jcasaslopez.notification.dto.Email;
import dev.jcasaslopez.notification.mapper.NotificationMapper;
import dev.jcasaslopez.notification.repository.FailedNotificationRepository;
import dev.jcasaslopez.notification.service.NotificationService;

@Component
@KafkaListener(topics = "${kafka.topic.name.notifications}")
public class NotificationListener {
	
	private final NotificationService notificationService;
	private final FailedNotificationRepository repository;
	private final NotificationMapper mapper;

	public NotificationListener(NotificationService notificationService, FailedNotificationRepository repository,
			NotificationMapper mapper) {
		this.notificationService = notificationService;
		this.repository = repository;
		this.mapper = mapper;
	}

	@KafkaHandler
	public void handler(Email email) throws InterruptedException {
	    if (!notificationService.trySendWithRetries(email)) {
	        repository.save(mapper.toEntity(email));
	    }
	}
}
