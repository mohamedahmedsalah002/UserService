package com.example.VirtualBank.service;

import com.example.VirtualBank.mapper.UserMapper;
import com.example.VirtualBank.model.dto.UserDto;
import com.example.VirtualBank.model.entity.User;
import com.example.VirtualBank.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
        return UserMapper.toDto(savedUser);
    }

    public Boolean deleteUser(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }
        
        return userRepository.findByUserId(userId)
                .map(user -> {
                    userRepository.delete(user);
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
        return UserMapper.toDto(savedUser);
    }
}
