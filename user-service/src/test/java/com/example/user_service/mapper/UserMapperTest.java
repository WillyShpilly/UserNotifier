package com.example.user_service.mapper;

import com.example.user_service.controller.dto.request.CreateUserRequest;
import com.example.user_service.controller.dto.request.UpdateUserRequest;
import com.example.user_service.controller.dto.response.UserResponse;
import com.example.user_service.entity.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    void toEntity_ShouldMapCreateUserRequestToUser() {
        // Given
        CreateUserRequest request = new CreateUserRequest("Beaves", "beaves@mtv.com", 47);

        // When
        User user = userMapper.toEntity(request);

        // Then
        assertNotNull(user);
        assertEquals("Beaves", user.getName());
        assertEquals("beaves@mtv.com", user.getEmail());
        assertEquals(47, user.getAge());
        assertNull(user.getId());
        assertNull(user.getCreated_at());
    }

    @Test
    void toResponse_ShouldMapUserToUserResponse() {
        // Given
        User user = new User("Beaves", "beaves@mtv.com", 47);
        user.setId(1L);
        LocalDateTime createdAt = LocalDateTime.now();
        user.setCreated_at(createdAt);

        // When
        UserResponse response = userMapper.toResponse(user);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Beaves", response.getName());
        assertEquals("beaves@mtv.com", response.getEmail());
        assertEquals(47, response.getAge());
        assertEquals(createdAt, response.getCreated_at());
    }

    @Test
    void updateEntityFromDto_ShouldUpdateOnlyNonNullFields() {
        // Given
        User existingUser = new User("Старое Имя", "old@test.com", 47);
        existingUser.setId(1L);
        existingUser.setCreated_at(LocalDateTime.now().minusDays(1));

        UpdateUserRequest request = new UpdateUserRequest("Новое Имя", null, 47);

        // When
        userMapper.updateEntityFromDto(request, existingUser);

        // Then
        assertEquals("Новое Имя", existingUser.getName());
        assertEquals("old@test.com", existingUser.getEmail());
        assertEquals(47, existingUser.getAge());
        assertEquals(1L, existingUser.getId());
        assertNotNull(existingUser.getCreated_at());
    }
}