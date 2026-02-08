package com.example.VirtualBank.event.producer;

import com.example.VirtualBank.common.constant.Topics;
import com.example.VirtualBank.event.model.BaseEvent;
import com.example.VirtualBank.event.model.CreateUserEvent;
import com.example.VirtualBank.event.model.UserUpdatedEvent;
import com.example.VirtualBank.event.model.UserDeletedEvent;
import com.example.VirtualBank.model.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
@Component
@RequiredArgsConstructor
@Slf4j
public class UserEventProducer implements BaseProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public CompletableFuture<Void> sendEvent(String topic, BaseEvent event) {
        return CompletableFuture.runAsync(() -> {
            kafkaTemplate.send(topic, event.getEventId().toString(), event)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("Failed to send event to topic: {}", topic, ex);
                        } else {
                            log.info("Event sent successfully to topic: {}", topic);
                        }
                    });
        });
    }

    @Override
    public CompletableFuture<Void> sendEvent(String topic, String key, BaseEvent event) {
        return CompletableFuture.runAsync(() -> {
            kafkaTemplate.send(topic, key, event);
        });
    }

    // User-specific methods
    public void publishUserCreated(User user) {
        CreateUserEvent event = CreateUserEvent.from(user);
        kafkaTemplate.send(Topics.USER_CREATED, user.getUserId().toString(), event);
        log.info("Published USER_CREATED event for userId: {}", user.getUserId());
    }

    public void publishUserUpdated(User user) {
        UserUpdatedEvent event = UserUpdatedEvent.from(user);
        kafkaTemplate.send(Topics.USER_UPDATED, user.getUserId().toString(), event);
        log.info("Published USER_UPDATED event for userId: {}", user.getUserId());
    }

    public void publishUserDeleted(UUID userId, String reason) {
        UserDeletedEvent event = UserDeletedEvent.from(userId, reason);
        kafkaTemplate.send(Topics.USER_DELETED, userId.toString(), event);
        log.info("Published USER_DELETED event for userId: {}", userId);
    }
}