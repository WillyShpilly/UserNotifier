package com.example.user_service.notification_service.consumer;

import com.example.user_service.notification_service.event.UserEvent;
import com.example.user_service.notification_service.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class UserEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(UserEventConsumer.class);

    private final EmailService emailService;

    public UserEventConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "user-created-events-topic")
    public void consumeUserCreatedEvent(UserEvent event) {
        logger.info("Получено событие создания пользователя: UserId: {}, Email: {}",
                event.getUserId(), event.getEmail());

        try {
            emailService.sendUserCreatedNotification(event.getEmail(), extractUserName(event.getEmail()));
        } catch (Exception e) {
            logger.error("Ошибка обработки события создания пользователя для {}: {}",
                    event.getEmail(), e.getMessage());
        }
    }

    @KafkaListener(topics = "user-deleted-events-topic")
    public void consumeUserDeletedEvent(UserEvent event) {
        logger.info("Получено событие удаления пользователя: UserId: {}, Email: {}",
                event.getUserId(), event.getEmail());

        try {
            emailService.sendUserDeletedNotification(event.getEmail(), extractUserName(event.getEmail()));
        } catch (Exception e) {
            logger.error("Ошибка обработки события удаления пользователя для {}: {}",
                    event.getEmail(), e.getMessage());
        }
    }

    private String extractUserName(String email) {
        if (email == null || !email.contains("@")) {
            return null;
        }
        return email.substring(0, email.indexOf('@'));
    }
}
