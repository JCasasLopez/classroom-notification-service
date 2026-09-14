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
	private String emailAddress;
	private String subject;
	private String emailBody;
	
	public FailedNotification(long idNotification, String emailAddress, String subject, String emailBody) {
		this.idNotification = idNotification;
		this.emailAddress = emailAddress;
		this.subject = subject;
		this.emailBody = emailBody;
	}

	public FailedNotification() {
	}

	public long getIdNotification() {
		return idNotification;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public String getSubject() {
		return subject;
	}

	public String getEmailBody() {
		return emailBody;
	}

	public void setIdNotification(long idNotification) {
		this.idNotification = idNotification;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setEmailBody(String emailBody) {
		this.emailBody = emailBody;
	}
	
}
