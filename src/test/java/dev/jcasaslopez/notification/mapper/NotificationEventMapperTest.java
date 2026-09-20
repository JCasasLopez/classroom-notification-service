package dev.jcasaslopez.notification.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import dev.jcasaslopez.classroom.shared.event.NotificationEvent;
import dev.jcasaslopez.notification.dto.Email;

class NotificationEventMapperTest {

    private final NotificationEventMapper mapper = new NotificationEventMapper();

    @Test
    void toEmail_mapped_successfully() {
        NotificationEvent event = new NotificationEvent("subject", "message", "test@example.com");

        Email email = mapper.toEmail(event);

        assertThat(email.subject()).isEqualTo("subject");
        assertThat(email.emailBody()).isEqualTo("message");
        assertThat(email.emailAddress()).isEqualTo("test@example.com");
    }

    @Test
    void toEvent_mapped_successfully() {
        Email email = new Email("subject", "body", "test@example.com");

        NotificationEvent event = mapper.toEvent(email);

        assertThat(event.subject()).isEqualTo("subject");
        assertThat(event.message()).isEqualTo("body");
        assertThat(event.emailAddress()).isEqualTo("test@example.com");
    }
}