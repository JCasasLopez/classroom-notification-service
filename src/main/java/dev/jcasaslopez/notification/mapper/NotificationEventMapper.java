package dev.jcasaslopez.notification.mapper;

import org.springframework.stereotype.Component;

import dev.jcasaslopez.classroom.shared.event.NotificationEvent;
import dev.jcasaslopez.notification.dto.Email;

@Component
public class NotificationEventMapper {

    public Email toEmail(NotificationEvent event) {
        return new Email(
                event.subject(),
                event.message(),      
                event.emailAddress()
        );
    }

    public NotificationEvent toEvent(Email email) {
        return new NotificationEvent(
                email.subject(),
                email.emailBody(),    
                email.emailAddress()
        );
    }
}