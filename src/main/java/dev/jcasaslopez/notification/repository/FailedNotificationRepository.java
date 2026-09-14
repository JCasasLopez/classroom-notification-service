package dev.jcasaslopez.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.jcasaslopez.notification.entity.FailedNotification;

public interface FailedNotificationRepository extends JpaRepository<FailedNotification, Integer> {

}
