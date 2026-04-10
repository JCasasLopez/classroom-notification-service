package dev.jcasaslopez.notification.service;

import dev.jcasaslopez.notification.model.Notification;

public interface NotificationService {
	
	void sendEmail(Notification notification);

}
