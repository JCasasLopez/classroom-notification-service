package dev.jcasaslopez.notification.model;

public class Email {
	private String emailAddress;
	private String subject;
	private String emailBody;
	
	public Email(String emailAddress, String subject, String emailBody) {
		this.emailAddress = emailAddress;
		this.subject = subject;
		this.emailBody = emailBody;
	}

	public Email() {
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
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