package dev.jcasaslopez.notification.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import dev.jcasaslopez.notification.dto.Email;
import dev.jcasaslopez.notification.entity.FailedNotification;

class NotificationMapperTest {

    private final NotificationMapper mapper = new NotificationMapper();

    @Test
    void toEntity_mapped_successfully() {
        Email email = new Email("subject", "body", "test@example.com");

        FailedNotification entity = mapper.toEntity(email);

        assertThat(entity.getIdNotification()).isEqualTo(0L);
        assertThat(entity.getSubject()).isEqualTo("subject");
        assertThat(entity.getEmailBody()).isEqualTo("body");
        assertThat(entity.getEmailAddress()).isEqualTo("test@example.com");
    }

    @Test
    void toEmail_mapped_successfully() {
        FailedNotification entity = new FailedNotification(1L, "subject", "body", "test@example.com");

        Email email = mapper.toEmail(entity);

        assertThat(email.subject()).isEqualTo("subject");
        assertThat(email.emailBody()).isEqualTo("body");
        assertThat(email.emailAddress()).isEqualTo("test@example.com");
    }
}