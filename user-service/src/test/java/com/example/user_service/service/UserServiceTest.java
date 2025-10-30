package com.example.user_service.service;

import com.example.user_service.controller.dto.request.CreateUserRequest;
import com.example.user_service.controller.dto.request.UpdateUserRequest;
import com.example.user_service.controller.dto.response.UserResponse;
import com.example.user_service.entity.User;
import com.example.user_service.mapper.UserMapper;
import com.example.user_service.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void createUser_ShouldReturnUserResponse_WhenValidRequest() {
        // Given
        CreateUserRequest request = new CreateUserRequest("Beavis", "Beavis@mtv.com", 47);
        User user = new User("Beavis", "Beavis@mtv.com", 47);
        User savedUser = new User("Beavis", "beavis@mtv.com", 47);
        savedUser.setId(1L);
        savedUser.setCreated_at(LocalDateTime.now());

        UserResponse expectedResponse = new UserResponse(1L, "Beavis", "beavis@mtv.com",
                47, savedUser.getCreated_at());

        when(userMapper.toEntity(request)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(savedUser);
        when(userMapper.toResponse(savedUser)).thenReturn(expectedResponse);

        // When
        UserResponse actualResponse = userService.createUser(request);

        // Then
        assertNotNull(actualResponse);
        assertEquals(1L, actualResponse.getId());
        assertEquals("Beavis", actualResponse.getName());
        assertEquals("beavis@mtv.com", actualResponse.getEmail());
        assertEquals(47, actualResponse.getAge());

        verify(userMapper).toEntity(request);
        verify(userRepository).save(user);
        verify(userMapper).toResponse(savedUser);
    }

    @Test
    void getUserById_ShouldThrowException_WhenUserNotFound() {
        // Given
        Long userId = 999L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.getUserById(userId));

        assertEquals("Пользователь не найден с id: 999", exception.getMessage());
        verify(userRepository).findById(userId);
        verify(userMapper, never()).toResponse(any());
    }

    @Test
    void getAllUsers_ShouldReturnListOfUserResponses() {
        // Given
        User user1 = new User("Beaves", "beaves@mtv.com", 47);
        user1.setId(1L);
        User user2 = new User("Butt-Head", "butt@head.com", 48);
        user2.setId(2L);

        List<User> users = Arrays.asList(user1, user2);

        UserResponse response1 = new UserResponse(1L, "Beaves", "beaves@mtv.com", 47, null);
        UserResponse response2 = new UserResponse(2L, "Butt-Head", "butt@head.com", 48, null);
        List<UserResponse> expectedResponses = Arrays.asList(response1, response2);

        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toResponseList(users)).thenReturn(expectedResponses);

        // When
        List<UserResponse> actualResponses = userService.getAllUsers();

        // Then
        assertNotNull(actualResponses);
        assertEquals(2, actualResponses.size());
        assertEquals("Beaves", actualResponses.get(0).getName());
        assertEquals("Butt-Head", actualResponses.get(1).getName());

        verify(userRepository).findAll();
        verify(userMapper).toResponseList(users);
    }

    @Test
    void updateUser_ShouldReturnUpdatedUserResponse_WhenUserExists() {
        // Given
        Long userId = 1L;
        UpdateUserRequest request = new UpdateUserRequest("Beaves Updated", "beaves.new@mtv.com", 15);

        User existingUser = new User("Beaves Old", "beaves.old@test.com", 47);
        existingUser.setId(userId);
        existingUser.setCreated_at(LocalDateTime.now().minusDays(1));

        User updatedUser = new User("Beaves Updated", "beaves.new@mtv.com", 15);
        updatedUser.setId(userId);
        updatedUser.setCreated_at(existingUser.getCreated_at());

        UserResponse expectedResponse = new UserResponse(userId, "Beaves Updated", "beaves.new@mtv.com", 15, updatedUser.getCreated_at());

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(updatedUser);
        when(userMapper.toResponse(updatedUser)).thenReturn(expectedResponse);

        // When
        UserResponse actualResponse = userService.updateUser(userId, request);

        // Then
        assertNotNull(actualResponse);
        assertEquals("Beaves Updated", actualResponse.getName());
        assertEquals("beaves.new@mtv.com", actualResponse.getEmail());
        assertEquals(15, actualResponse.getAge());

        verify(userRepository).findById(userId);
        verify(userMapper).updateEntityFromDto(request, existingUser);
        verify(userRepository).save(existingUser);
        verify(userMapper).toResponse(updatedUser);
    }

    @Test
    void deleteUser_ShouldDeleteUser_WhenUserExists() {
        // Given
        Long userId = 1L;
        User user = new User("Beaves Old", "beaves.old@test.com", 47);
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // When
        userService.deleteUser(userId);

        // Then
        verify(userRepository).findById(userId);
        verify(userRepository).delete(user);
    }

    @Test
    void deleteUser_ShouldThrowException_WhenUserNotFound() {
        // Given
        Long userId = 999L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.deleteUser(userId));

        assertEquals("Пользователь не найден с id: 999", exception.getMessage());
        verify(userRepository).findById(userId);
        verify(userRepository, never()).delete(any());
    }
}
