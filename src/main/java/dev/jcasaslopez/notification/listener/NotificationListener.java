package dev.jcasaslopez.notification.listener;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import dev.jcasaslopez.classroom.shared.event.NotificationEvent;
import dev.jcasaslopez.notification.dto.Email;
import dev.jcasaslopez.notification.mapper.NotificationEventMapper;
import dev.jcasaslopez.notification.mapper.NotificationMapper;
import dev.jcasaslopez.notification.repository.FailedNotificationRepository;
import dev.jcasaslopez.notification.service.NotificationService;

@Component
@KafkaListener(topics = "${kafka.topic.name.notifications}")
public class NotificationListener {
	
	private final NotificationService notificationService;
	private final FailedNotificationRepository repository;
	private final NotificationMapper notificationMapper;
	private final NotificationEventMapper notificationEventMapper;

	public NotificationListener(NotificationService notificationService, FailedNotificationRepository repository,
			NotificationMapper notificationMapper, NotificationEventMapper notificationEventMapper) {
		this.notificationService = notificationService;
		this.repository = repository;
		this.notificationMapper = notificationMapper;
		this.notificationEventMapper = notificationEventMapper;
	}

	@KafkaHandler
	public void handler(NotificationEvent notificationEvent) throws InterruptedException {
		Email email = notificationEventMapper.toEmail(notificationEvent);
	    if (!notificationService.trySendWithRetries(email)) {
	        repository.save(notificationMapper.toEntity(email));
	    }
	}
}
