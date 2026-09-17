package dev.jcasaslopez.notification.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import dev.jcasaslopez.notification.dto.Email;
import dev.jcasaslopez.notification.entity.FailedNotification;
import dev.jcasaslopez.notification.exception.EmailSendException;
import dev.jcasaslopez.notification.mapper.NotificationMapper;
import dev.jcasaslopez.notification.repository.FailedNotificationRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class NotificationServiceImpl implements NotificationService {

	private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);
    private static final int MAX_ATTEMPTS = 5;

	private final JavaMailSender mailSender;
	private final FailedNotificationRepository repository;
	private final NotificationMapper mapper;

	public NotificationServiceImpl(JavaMailSender mailSender, FailedNotificationRepository repository,
			NotificationMapper mapper) {
		this.mailSender = mailSender;
		this.repository = repository;
		this.mapper = mapper;
	}

	@Override
	public void sendEmail(Email notification) throws EmailSendException {

		String emailAddress = notification.emailAddress();
		String emailSubject = notification.subject();
		String emailBody = notification.emailBody();

		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

			helper.setTo(emailAddress);
			helper.setSubject(emailSubject);
			helper.setText(emailBody, true); 

			mailSender.send(mimeMessage);

			logger.info("Email successfully sent to: {}", emailAddress);

		} catch (RuntimeException | MessagingException ex) {
			logger.error("Failed to send email to {}: {}", emailAddress, ex.getMessage(), ex);
			throw new EmailSendException("Fail to send email");
		}

	}
	
	@Override
	public boolean trySendWithRetries(Email email) throws InterruptedException {
        int retries = 0;
        while (retries < MAX_ATTEMPTS) {
            try {
                sendEmail(email);
                return true;
            } catch (EmailSendException ex) {
                retries++;
                Thread.sleep(1000);
            }
        }
        return false;
    }
	
	@Scheduled(fixedDelay = 60000)
	public void retryFailedNotifications() throws InterruptedException {
	    List<FailedNotification> pending = repository.findAll();

	    for (FailedNotification failed : pending) {
	        Email email = mapper.toEmail(failed);

	        if (trySendWithRetries(email)) {
	            repository.delete(failed); 
	        }
	    }
	}
	
}