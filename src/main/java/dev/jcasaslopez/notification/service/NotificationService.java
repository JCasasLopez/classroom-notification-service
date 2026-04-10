package dev.jcasaslopez.notification.service;

import dev.jcasaslopez.notification.model.Email;

public interface NotificationService {
	
	void sendEmail(Email notification);

}
