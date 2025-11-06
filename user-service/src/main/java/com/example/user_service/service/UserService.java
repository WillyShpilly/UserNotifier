package com.example.user_service.service;

import com.example.user_service.controller.dto.request.CreateUserRequest;
import com.example.user_service.controller.dto.request.UpdateUserRequest;
import com.example.user_service.controller.dto.response.UserResponse;
import com.example.user_service.entity.User;
import com.example.user_service.event.producer.UserEventProducer;
import com.example.user_service.mapper.UserMapper;
import com.example.user_service.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserEventProducer userEventProducer;

    public UserService(UserRepository userRepository,
                       UserMapper userMapper,
                       UserEventProducer userEventProducer) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userEventProducer = userEventProducer;

    }

    public UserResponse createUser(CreateUserRequest request) {
        User user = userMapper.toEntity(request);
        User savedUser = userRepository.save(user);

        userEventProducer.sendUserCreatedEvent(savedUser.getId(), savedUser.getEmail());

        return userMapper.toResponse(savedUser);
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден с id: " + id));
        return userMapper.toResponse(user);
    }

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userMapper.toResponseList(users);
    }

    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден с id: " + id));

        userMapper.updateEntityFromDto(request, user);
        User updatedUser = userRepository.save(user);

        return userMapper.toResponse(updatedUser);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден с id: " + id));

        Long userId = user.getId();
        String userEmail = user.getEmail();

        userRepository.delete(user);

        userEventProducer.sendUserDeletedEvent(userId, userEmail);
    }

    public User getUserEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден с id: " + id));
    }

    public List<User> getAllUsersEntities() {
        return userRepository.findAll();
    }

    public User createUserEntity(CreateUserRequest request) {
        User user = userMapper.toEntity(request);
        User savedUser = userRepository.save(user);
        userEventProducer.sendUserCreatedEvent(savedUser.getId(), savedUser.getEmail());
        return savedUser;
    }

    public User updateUserEntity(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден с id: " + id));
        userMapper.updateEntityFromDto(request, user);
        return userRepository.save(user);
    }
}
