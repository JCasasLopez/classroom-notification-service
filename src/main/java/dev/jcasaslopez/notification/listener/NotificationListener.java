package dev.jcasaslopez.notification.listener;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import dev.jcasaslopez.notification.model.Email;
import dev.jcasaslopez.notification.service.NotificationService;

@Component
@KafkaListener(topics = "${kafka.topic.name.notifications}")
public class NotificationListener {
	
	private final NotificationService notificationService;
	
	public NotificationListener(NotificationService notificationService) {
		this.notificationService = notificationService;
	}

	@KafkaHandler
	public void handler(Email email) {
		notificationService.sendEmail(email);
	}

}
