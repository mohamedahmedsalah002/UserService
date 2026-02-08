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
        log.info("Processing event - Type: {}, ID: {}, Timestamp: {}", 
                event.getEventType(), event.getEventId(), event.getTimestamp());
        
        if (event instanceof CreateUserEvent createEvent) {
            handleUserCreated(createEvent);
        } else if (event instanceof UserUpdatedEvent updateEvent) {
            handleUserUpdated(updateEvent);
        } else if (event instanceof UserDeletedEvent deleteEvent) {
            handleUserDeleted(deleteEvent);
        } else {
            log.warn("Unknown event type: {}", event.getEventType());
        }
    }

    private void handleUserCreated(CreateUserEvent event) {
        CreateUserEvent.UserEventData userData = event.getData();
        
        log.info("=== User Created Event Processing ===");
        log.info("Event ID: {}", event.getEventId());
        log.info("Service: {}", event.getServiceName());
        log.info("User ID: {}", userData.getUserId());
        log.info("Username: {}", userData.getUsername());
        log.info("Email: {}", userData.getEmail());
        log.info("Created At: {}", event.getTimestamp());
        log.info("=====================================");
        
        // TODO: Add business logic here
        // Examples:
        // - Send welcome email to user
        // - Update Redis cache
        // - Sync to Elasticsearch for search
        // - Send to analytics service
        // - Create default user settings
        
        log.info("✅ Successfully processed USER_CREATED event for user: {}", userData.getUsername());
    }

    private void handleUserUpdated(UserUpdatedEvent event) {
        UserUpdatedEvent.UserUpdateEventData userData = event.getData();
        
        log.info("=== User Updated Event Processing ===");
        log.info("Event ID: {}", event.getEventId());
        log.info("Service: {}", event.getServiceName());
        log.info("User ID: {}", userData.getUserId());
        log.info("Username: {}", userData.getUsername());
        log.info("Email: {}", userData.getEmail());
        log.info("Updated At: {}", event.getTimestamp());
        log.info("=====================================");
        
        // TODO: Add business logic here
        // Examples:
        // - Update cache with new user data
        // - Send email notification about profile changes
        // - Update search index
        // - Notify connected devices
        // - Log to audit service
        
        log.info("✅ Successfully processed USER_UPDATED event for user: {}", userData.getUsername());
    }

    private void handleUserDeleted(UserDeletedEvent event) {
        UserDeletedEvent.UserDeleteEventData userData = event.getData();
        
        log.info("=== User Deleted Event Processing ===");
        log.info("Event ID: {}", event.getEventId());
        log.info("Service: {}", event.getServiceName());
        log.info("User ID: {}", userData.getUserId());
        log.info("Reason: {}", userData.getReason());
        log.info("Deleted At: {}", event.getTimestamp());
        log.info("=====================================");
        
        // TODO: Add business logic here
        // Examples:
        // - Remove from cache
        // - Delete user data from other services
        // - Send goodbye email
        // - Archive user data for compliance
        // - Remove from search index
        // - Cancel subscriptions
        
        log.info("✅ Successfully processed USER_DELETED event for user ID: {}", userData.getUserId());
    }

    @Override
    public void handleError(BaseEvent event, Throwable throwable) {
        log.error("❌ Error processing event");
        log.error("Event Type: {}", event.getEventType());
        log.error("Event ID: {}", event.getEventId());
        log.error("Service Name: {}", event.getServiceName());
        log.error("Timestamp: {}", event.getTimestamp());
        log.error("Error Message: {}", throwable.getMessage());
        log.error("Error Class: {}", throwable.getClass().getName());
        log.error("Stack Trace: ", throwable);
        
        // TODO: Implement advanced error handling
        
        // 1. Send to Dead Letter Queue (DLQ)
        // kafkaTemplate.send("user-events-dlq", event.getEventId().toString(), event);
        // log.info("Event sent to Dead Letter Queue for manual review");
        
        // 2. Alert Monitoring System
        // alertService.sendAlert("Kafka Consumer Error", 
        //     "Failed to process event: " + event.getEventType());
        
        // 3. Save to Error Table for Retry
        // errorRepository.save(new EventError(event, throwable));
        
        // 4. Metrics/Monitoring
        // metrics.incrementCounter("kafka.consumer.error", 
        //     "event_type", event.getEventType());
        
        log.warn("⚠️ Event processing failed - manual intervention may be required");
    }

    @Override
    public boolean canHandle(String eventType) {
        return eventType.startsWith("USER_");
    }
}