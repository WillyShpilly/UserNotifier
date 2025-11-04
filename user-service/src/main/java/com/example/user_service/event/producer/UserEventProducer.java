package com.example.user_service.event.producer;

import com.example.user_service.event.UserEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class UserEventProducer {

    private static final Logger logger = LoggerFactory.getLogger(UserEventProducer.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public UserEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendUserCreatedEvent(Long userId, String email) {
        UserEvent event = new UserEvent(
                userId,
                email,
                "USER_CREATED",
                "Пользователь с email " + email + " был создан"
        );
        sendEvent("user-created-events-topic", userId, event, "создания");
    }

    public void sendUserDeletedEvent(Long userId, String email) {
        UserEvent event = new UserEvent(
                userId,
                email,
                "USER_DELETED",
                "Пользователь с email " + email + " был удален"
        );
        sendEvent("user-deleted-events-topic", userId, event, "удаления");
    }

    private void sendEvent(String topic, Long userId, Object event, String eventType) {
        try {
            CompletableFuture<SendResult<String, Object>> future =
                    kafkaTemplate.send(topic, userId.toString(), event);

            future.whenComplete((result, exception) -> {
                if (exception == null) {
                    logger.info("Сообщение о {} отправлено в Kafka. UserId: {}, Topic: {}, Offset: {}, Partition: {}",
                            eventType,
                            userId,
                            result.getRecordMetadata().topic(),
                            result.getRecordMetadata().offset(),
                            result.getRecordMetadata().partition());
                } else {
                    logger.error("Ошибка отправки сообщения о {} в Kafka для userId {}: {}",
                            eventType,
                            userId, exception.getMessage());
                }
            });

        } catch (Exception e) {
            logger.error("Ошибка при создании события {} для userId {}: {}",
                    eventType, userId, e.getMessage());
        }
    }
}
