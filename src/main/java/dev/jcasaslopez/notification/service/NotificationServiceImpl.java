package dev.jcasaslopez.notification.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import dev.jcasaslopez.notification.model.Email;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class NotificationServiceImpl implements NotificationService {

	private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

	private JavaMailSender mailSender;

	public NotificationServiceImpl(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	// Message format is HTTP.
	@Override
	public void sendEmail(Email notification) {

		String emailAddress = notification.getEmailAddress();
		String emailSubject = notification.getSubject();
		String emailBody = notification.getEmailBody();

		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

			helper.setTo(emailAddress);
			helper.setSubject(emailSubject);
			helper.setText(emailBody, true); 

			mailSender.send(mimeMessage);

			logger.info("Email successfully sent to: {}", emailAddress);

		} catch (MessagingException ex) {
			logger.error("Failed to send email to {}: {}", emailAddress, ex.getMessage(), ex);
		}

	}

}