package dev.jcasaslopez.notification.base;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;

import dev.jcasaslopez.notification.repository.FailedNotificationRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class BaseIntegrationTest {
	
	@Autowired protected ObjectMapper objectMapper;
    @Autowired protected FailedNotificationRepository repository;
    @Autowired protected JavaMailSenderImpl mailSender;
    @Autowired protected KafkaListenerEndpointRegistry registry; 

    @RegisterExtension
	protected static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withDisabledAuthentication());
    
    @ServiceConnection
    static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.3");

    @ServiceConnection
    static final KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.6.0"));

    static {
        mySQLContainer.start();
        kafkaContainer.start();
    }
    
    @BeforeEach
    void pointMailSenderToGreenMail() {
        mailSender.setHost(greenMail.getSmtp().getBindTo());
        mailSender.setPort(greenMail.getSmtp().getPort());
    }
    
    @BeforeEach 
    void waitForKafkaListenerAssignment() {
        for (MessageListenerContainer container : registry.getListenerContainers()) {
            ContainerTestUtils.waitForAssignment(container, 1); 
        }
    }
    
    @AfterEach
    void cleanDatabase() {
        repository.deleteAllInBatch();
        greenMail.reset();
    }

}
