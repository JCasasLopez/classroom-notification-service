package dev.jcasaslopez.notification.mapper;

import org.springframework.stereotype.Component;

import dev.jcasaslopez.notification.dto.Email;
import dev.jcasaslopez.notification.entity.FailedNotification;

@Component
public class NotificationMapper {

    public FailedNotification toEntity(Email email) {
        return new FailedNotification(
                0L, 
                email.subject(),
                email.emailBody(),
                email.emailAddress()
        );
    }

    public Email toEmail(FailedNotification entity) {
        return new Email(
                entity.getSubject(),      
                entity.getEmailBody(),
                entity.getEmailAddress()
        );
    }
}