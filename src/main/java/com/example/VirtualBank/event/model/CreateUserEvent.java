package com.example.VirtualBank.event.model;

import com.example.VirtualBank.model.entity.User;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserEvent implements BaseEvent {

    private UUID eventId;
    private String eventType;
    private LocalDateTime timestamp;
    private String serviceName;
    private UserEventData data; // Change from Object to specific type

    // Inner class for event data
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserEventData {
        private UUID userId;
        private String username;
        private String email;
    }

    // Factory method
    public static CreateUserEvent from(User user) {
        return CreateUserEvent.builder()
                .eventId(UUID.randomUUID())
                .eventType("USER_CREATED")
                .timestamp(LocalDateTime.now())
                .serviceName("user-service")
                .data(UserEventData.builder()
                        .userId(user.getUserId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .build())
                .build();
    }
}