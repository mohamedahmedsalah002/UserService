package com.example.VirtualBank.service;

import com.example.VirtualBank.event.producer.UserEventProducer;
import com.example.VirtualBank.mapper.UserMapper;
import com.example.VirtualBank.model.dto.UserDto;
import com.example.VirtualBank.model.entity.User;
import com.example.VirtualBank.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserEventProducer eventProducer;

    public UserService(UserRepository userRepository, UserEventProducer eventProducer) {
        this.userRepository = userRepository;
        this.eventProducer = eventProducer;
    }


    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return UserMapper.toDtoList(users);
    }

    public UserDto createUser(UserDto userDto) {
        if (userDto == null) {
            throw new IllegalArgumentException("UserDto cannot be null");
        }
        
        User user = UserMapper.toEntity(userDto);
        User savedUser = userRepository.save(user);
        
        // Publish user created event
        try {
            log.info("Attempting to publish USER_CREATED event for userId: {}", savedUser.getUserId());
            eventProducer.publishUserCreated(savedUser);
            log.info("Successfully published USER_CREATED event for userId: {}", savedUser.getUserId());
        } catch (Exception e) {
            log.error("Failed to publish USER_CREATED event for userId: {}, Error: {}", savedUser.getUserId(), e.getMessage(), e);
            // Continue execution even if event publishing fails
        }
        
        return UserMapper.toDto(savedUser);
    }

    public Boolean deleteUser(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }
        
        return userRepository.findByUserId(userId)
                .map(user -> {
                    userRepository.delete(user);
                    
                    // Publish user deleted event
                    try {
                        eventProducer.publishUserDeleted(userId, "User deleted by request");
                        log.info("Published USER_DELETED event for userId: {}", userId);
                    } catch (Exception e) {
                        log.error("Failed to publish USER_DELETED event for userId: {}", userId, e);
                    }
                    
                    return true;
                }).orElse(false);
    }

    public UserDto updateUser(UUID userId, UserDto userDto) {
        if (userId == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }
        if (userDto == null) {
            throw new IllegalArgumentException("UserDto cannot be null");
        }
        
        User existingUser = userRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // Update only allowed fields
        existingUser.setUsername(userDto.getUsername());
        existingUser.setEmail(userDto.getEmail());
        User savedUser = userRepository.save(existingUser);
        
        // Publish user updated event
        try {
            eventProducer.publishUserUpdated(savedUser);
            log.info("Published USER_UPDATED event for userId: {}", userId);
        } catch (Exception e) {
            log.error("Failed to publish USER_UPDATED event for userId: {}", userId, e);
        }
        
        return UserMapper.toDto(savedUser);
    }
}
