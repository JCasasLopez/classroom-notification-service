package dev.jcasaslopez.notification.exception;

public class EmailSendException extends RuntimeException {
	public EmailSendException(String message) {
        super(message);
    }
}
