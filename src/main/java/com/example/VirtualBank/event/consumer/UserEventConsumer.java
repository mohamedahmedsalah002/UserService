package com.example.VirtualBank.event.consumer;

import com.example.VirtualBank.common.constant.Topics;
import com.example.VirtualBank.event.model.BaseEvent;
import com.example.VirtualBank.event.model.CreateUserEvent;
import com.example.VirtualBank.event.model.UserUpdatedEvent;
import com.example.VirtualBank.event.model.UserDeletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserEventConsumer implements BaseConsumer {

    // Inject any services you need (e.g., NotificationService, AuditService)

    @KafkaListener(topics = Topics.USER_CREATED, groupId = "user-service-group")
    public void consumeUserCreatedEvent(CreateUserEvent event) {
        log.info("Received USER_CREATED event: {}", event.getEventId());
        try {
            processEvent(event);
        } catch (Exception e) {
            handleError(event, e);
        }
    }

    @KafkaListener(topics = Topics.USER_UPDATED, groupId = "user-service-group")
    public void consumeUserUpdatedEvent(UserUpdatedEvent event) {
        log.info("Received USER_UPDATED event: {}", event.getEventId());
        try {
            processEvent(event);
        } catch (Exception e) {
            handleError(event, e);
        }
    }

    @KafkaListener(topics = Topics.USER_DELETED, groupId = "user-service-group")
    public void consumeUserDeletedEvent(UserDeletedEvent event) {
        log.info("Received USER_DELETED event: {}", event.getEventId());
        try {
            processEvent(event);
        } catch (Exception e) {
            handleError(event, e);
        }
    }

    @Override
    public void processEvent(BaseEvent event) {
        // Implement business logic for each event type
        if (event instanceof CreateUserEvent) {
            // Handle user creation (e.g., send notification, log audit)
        } else if (event instanceof UserUpdatedEvent) {
            // Handle user update
        } else if (event instanceof UserDeletedEvent) {
            // Handle user deletion
        }
    }

    @Override
    public void handleError(BaseEvent event, Throwable throwable) {
        log.error("Error processing event: {}", event.getEventType(), throwable);
        // Implement error handling:
        // - Send to dead letter queue
        // - Alert monitoring system
        // - Retry logic
    }

    @Override
    public boolean canHandle(String eventType) {
        return eventType.startsWith("USER_");
    }
}