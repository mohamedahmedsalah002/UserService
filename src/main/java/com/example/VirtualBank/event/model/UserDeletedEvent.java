package com.example.VirtualBank.event.model;

import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDeletedEvent implements BaseEvent {

    private UUID eventId;
    private String eventType;
    private LocalDateTime timestamp;
    private String serviceName;
    private UserDeleteEventData data; // Specific type

    // Inner class for delete event data
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserDeleteEventData {
        private UUID userId;
        private String reason; // Optional: why deleted
    }

    // Factory method
    public static UserDeletedEvent from(UUID userId, String reason) {
        return UserDeletedEvent.builder()
                .eventId(UUID.randomUUID())
                .eventType("USER_DELETED")
                .timestamp(LocalDateTime.now())
                .serviceName("user-service")
                .data(UserDeleteEventData.builder()
                        .userId(userId)
                        .reason(reason)
                        .build())
                .build();
    }
}