package com.example.VirtualBank.service;

import com.example.VirtualBank.model.dto.UserDto;
import com.example.VirtualBank.model.entity.User;
import com.example.VirtualBank.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private UserDto testUserDto;
    private UUID testUserId;

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();
        
        testUser = User.builder()
                .id(1L)
                .userId(testUserId)
                .username("testuser")
                .email("test@example.com")
                .password("password123")
                .build();

        testUserDto = UserDto.builder()
                .id(1L)
                .userId(testUserId)
                .username("testuser")
                .email("test@example.com")
                .build();
    }

    @Test
    void getAllUsers_ShouldReturnListOfUserDtos_WhenUsersExist() {
        // Given
        List<User> users = Arrays.asList(testUser);
        when(userRepository.findAll()).thenReturn(users);

        // When
        List<UserDto> result = userService.getAllUsers();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testUser.getUsername(), result.get(0).getUsername());
        assertEquals(testUser.getEmail(), result.get(0).getEmail());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getAllUsers_ShouldReturnEmptyList_WhenNoUsersExist() {
        // Given
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<UserDto> result = userService.getAllUsers();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void createUser_ShouldReturnUserDto_WhenValidUserDto() {
        // Given
        User savedUser = User.builder()
                .id(1L)
                .userId(testUserId)
                .username("newuser")
                .email("newuser@example.com")
                .build();

        UserDto inputDto = UserDto.builder()
                .username("newuser")
                .email("newuser@example.com")
                .build();

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // When
        UserDto result = userService.createUser(inputDto);

        // Then
        assertNotNull(result);
        assertEquals(savedUser.getUsername(), result.getUsername());
        assertEquals(savedUser.getEmail(), result.getEmail());
        assertEquals(savedUser.getUserId(), result.getUserId());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUser_ShouldThrowException_WhenUserDtoIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(null);
        });

        verify(userRepository, never()).save(any(User.class));
    }


    @Test
    void deleteUser_ShouldReturnTrue_WhenUserExists() {
        // Given
        when(userRepository.findByUserId(testUserId)).thenReturn(Optional.of(testUser));
        doNothing().when(userRepository).delete(testUser);

        // When
        Boolean result = userService.deleteUser(testUserId);

        // Then
        assertTrue(result);
        verify(userRepository, times(1)).findByUserId(testUserId);
        verify(userRepository, times(1)).delete(testUser);
    }

    @Test
    void deleteUser_ShouldReturnFalse_WhenUserDoesNotExist() {
        // Given
        when(userRepository.findByUserId(testUserId)).thenReturn(Optional.empty());

        // When
        Boolean result = userService.deleteUser(testUserId);

        // Then
        assertFalse(result);
        verify(userRepository, times(1)).findByUserId(testUserId);
        verify(userRepository, never()).delete(any(User.class));
    }

    @Test
    void deleteUser_ShouldThrowException_WhenUserIdIsNull() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            userService.deleteUser(null);
        });
        
        verify(userRepository, never()).findByUserId(any());
        verify(userRepository, never()).delete(any(User.class));
    }

    @Test
    void updateUser_ShouldReturnUpdatedUserDto_WhenUserExists() {
        // Given
        UserDto updateDto = UserDto.builder()
                .username("updateduser")
                .email("updated@example.com")
                .build();

        User updatedUser = User.builder()
                .id(testUser.getId())
                .userId(testUser.getUserId())
                .username("updateduser")
                .email("updated@example.com")
                .password(testUser.getPassword())
                .build();

        when(userRepository.findByUserId(testUserId)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        // When
        UserDto result = userService.updateUser(testUserId, updateDto);

        // Then
        assertNotNull(result);
        assertEquals(updateDto.getUsername(), result.getUsername());
        assertEquals(updateDto.getEmail(), result.getEmail());
        assertEquals(testUserId, result.getUserId());
        verify(userRepository, times(1)).findByUserId(testUserId);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUser_ShouldThrowRuntimeException_WhenUserDoesNotExist() {
        // Given
        UserDto updateDto = UserDto.builder()
                .username("updateduser")
                .email("updated@example.com")
                .build();

        when(userRepository.findByUserId(testUserId)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.updateUser(testUserId, updateDto);
        });

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findByUserId(testUserId);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateUser_ShouldThrowException_WhenUserIdIsNull() {
        // Given
        UserDto updateDto = UserDto.builder()
                .username("updateduser")
                .email("updated@example.com")
                .build();

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            userService.updateUser(null, updateDto);
        });
        
        verify(userRepository, never()).findByUserId(any());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateUser_ShouldThrowException_WhenUserDtoIsNull() {

        assertThrows(IllegalArgumentException.class, () -> {
            userService.updateUser(testUserId, null);
        });

        verify(userRepository, never()).findByUserId(any());
        verify(userRepository, never()).save(any(User.class));
    }


    @Test
    void updateUser_ShouldPreserveUserIdAndId_WhenUpdating() {
        // Given
        UserDto updateDto = UserDto.builder()
                .username("newusername")
                .email("newemail@example.com")
                .build();

        User updatedUser = User.builder()
                .id(testUser.getId())
                .userId(testUser.getUserId())
                .username("newusername")
                .email("newemail@example.com")
                .password(testUser.getPassword())
                .build();

        when(userRepository.findByUserId(testUserId)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        // When
        UserDto result = userService.updateUser(testUserId, updateDto);

        // Then
        assertEquals(testUser.getId(), result.getId());
        assertEquals(testUser.getUserId(), result.getUserId());
        assertEquals(updateDto.getUsername(), result.getUsername());
        assertEquals(updateDto.getEmail(), result.getEmail());
    }
}