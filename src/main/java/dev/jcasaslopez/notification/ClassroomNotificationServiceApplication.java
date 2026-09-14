package dev.jcasaslopez.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ClassroomNotificationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClassroomNotificationServiceApplication.class, args);
	}

}
