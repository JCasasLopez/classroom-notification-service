package dev.jcasaslopez.notification.model;

public class Notification {
	private String email;
	private String subject;
	private String emailBody;
	public Notification(String email, String subject, String emailBody) {
		this.email = email;
		this.subject = subject;
		this.emailBody = emailBody;
	}

	public Notification() {
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getEmailBody() {
		return emailBody;
	}

	public void setEmailBody(String emailBody) {
		this.emailBody = emailBody;
	}

}