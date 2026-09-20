package dev.jcasaslopez.notification.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="failed_notifications")
public class FailedNotification {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long idNotification;
	private String subject;
	private String emailBody;
	private String emailAddress;

	public FailedNotification(long idNotification, String subject, String emailBody, String emailAddress) {
		this.idNotification = idNotification;
		this.subject = subject;
		this.emailBody = emailBody;
		this.emailAddress = emailAddress;
	}

	public FailedNotification() {
	}

	public long getIdNotification() {
		return idNotification;
	}

	public String getSubject() {
		return subject;
	}

	public String getEmailBody() {
		return emailBody;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setIdNotification(long idNotification) {
		this.idNotification = idNotification;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setEmailBody(String emailBody) {
		this.emailBody = emailBody;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

}
