package dev.jcasaslopez.notification.service;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

import dev.jcasaslopez.classroom.shared.enums.NotificationType;
import dev.jcasaslopez.classroom.shared.event.NotificationEvent;
import dev.jcasaslopez.notification.base.BaseIntegrationTest;
import dev.jcasaslopez.notification.entity.FailedNotification;
import jakarta.mail.internet.MimeMessage;

public class NotificationServiceIntegrationTest extends BaseIntegrationTest {

	@Value("${kafka.topic.name.notifications}") private String topicName;
	@Autowired private KafkaTemplate<String, NotificationEvent> kafkaTemplate;

	// Use the implementation, since the method we need is not part of the interface
	@Autowired private NotificationServiceImpl notificationService;

	private static final String EMAIL_ADDRESS = "user@example.com";

	@Test
	void email_is_sent_successfully() throws InterruptedException, ExecutionException, TimeoutException {
		// Arrange
		NotificationEvent event = new NotificationEvent(
				NotificationType.CREATE_ACCOUNT.getSubject(),
				NotificationType.CREATE_ACCOUNT.getMessageText(),
				EMAIL_ADDRESS);

		// Act
		kafkaTemplate.send(topicName, event).get(5, TimeUnit.SECONDS);

		// Assert
		boolean received = greenMail.waitForIncomingEmail(5000, 1);
		assertTrue(received);

		MimeMessage[] messages = greenMail.getReceivedMessages();
		assertTrue(messages.length==1);

		MimeMessage message = messages[0];

		assertAll(
				() -> assertEquals(NotificationType.CREATE_ACCOUNT.getSubject(), message.getSubject()),
				() -> assertEquals(EMAIL_ADDRESS, message.getAllRecipients()[0].toString()),
				() -> assertEquals(0, repository.count())
				);

	}

	@Test
	void email_fails_on_first_attempt_and_succeeds_after_recovery() throws InterruptedException, ExecutionException, TimeoutException {
	    // --- PHASE 1: Temporary SMTP Outage ---

	    // Arrange
	    mailSender.setPort(9999);

	    NotificationEvent event = new NotificationEvent(
	            NotificationType.CREATE_ACCOUNT.getSubject(),
	            NotificationType.CREATE_ACCOUNT.getMessageText(),
	            EMAIL_ADDRESS
	            );

	    // Act
	    kafkaTemplate.send(topicName, event).get(5, TimeUnit.SECONDS);

	    // Assert - wait until the failure has actually been persisted
	    await().atMost(5, SECONDS).untilAsserted(() ->
	            assertEquals(1, repository.count())
	            );

	    // --- PHASE 2: Service Recovery & Scheduled Retry ---

	    // Arrange
	    mailSender.setPort(greenMail.getSmtp().getPort());

	    // Act
	    notificationService.retryFailedNotifications();

	    // Assert
	    await().atMost(5, SECONDS).untilAsserted(() -> {
	        assertAll(
	                () -> assertTrue(greenMail.waitForIncomingEmail(1000, 1)),
	                () -> assertEquals(0, repository.count())
	                );
	    });

	}

	@Test
	void email_fails_on_first_attempt_and_keeps_failing_after_retrying() throws InterruptedException, ExecutionException, TimeoutException {
	    // Simulate SMTP service outage by pointing JavaMailSender to an invalid port
	    mailSender.setPort(9999);

	    NotificationEvent event = new NotificationEvent(
	            NotificationType.CREATE_ACCOUNT.getSubject(),
	            NotificationType.CREATE_ACCOUNT.getMessageText(),
	            EMAIL_ADDRESS
	            );

	    // Act
	    kafkaTemplate.send(topicName, event).get(5, TimeUnit.SECONDS);

	    // Wait until the initial failure has actually been persisted
	    await().atMost(5, SECONDS).untilAsserted(() ->
	            assertEquals(1, repository.count())
	            );

	    notificationService.retryFailedNotifications();

	    // Assert
	    await().atMost(5, SECONDS).untilAsserted(() -> {
	        List<FailedNotification> all = repository.findAll();
	        assertEquals(1, all.size()); // throws AssertionError -> Awaitility retries
	        FailedNotification persistedFailedNotification = all.get(0); // Now it is safe to retrieve it

	        assertAll(
	                () -> assertTrue(greenMail.getReceivedMessages().length == 0),
	                () -> assertEquals(1, repository.count()),
	                () -> assertEquals(event.subject(), persistedFailedNotification.getSubject()),
	                () -> assertEquals(event.message(), persistedFailedNotification.getEmailBody()),
	                () -> assertEquals(EMAIL_ADDRESS, persistedFailedNotification.getEmailAddress())
	                );
	    });
	}

}
