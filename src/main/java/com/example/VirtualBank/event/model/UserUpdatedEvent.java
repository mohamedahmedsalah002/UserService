package com.example.VirtualBank.event.model;

import com.example.VirtualBank.model.entity.User;
import lombok.*;


import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdatedEvent implements BaseEvent {

    private UUID eventId;
    private String eventType;
    private LocalDateTime timestamp;
    private String serviceName;
    private UserUpdateEventData data; // Specific type

    // Inner class for update event data
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserUpdateEventData {
        private UUID userId;
        private String username;
        private String email;
        private Map<String, Object> changes; // Optional: track what changed
    }

    // Factory method
    public static UserUpdatedEvent from(User user) {
        return UserUpdatedEvent.builder()
                .eventId(UUID.randomUUID())
                .eventType("USER_UPDATED")
                .timestamp(LocalDateTime.now())
                .serviceName("user-service")
                .data(UserUpdateEventData.builder()
                        .userId(user.getUserId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .build())
                .build();
    }
}