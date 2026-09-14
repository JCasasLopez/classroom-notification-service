package dev.jcasaslopez.notification.service;

import dev.jcasaslopez.notification.dto.Email;
import dev.jcasaslopez.notification.exception.EmailSendException;

public interface NotificationService {
	
	void sendEmail(Email notification) throws EmailSendException;
	boolean trySendWithRetries(Email email) throws InterruptedException;

}
