package com.example.user_service.controller;

import com.example.user_service.controller.dto.request.CreateUserRequest;
import com.example.user_service.controller.dto.request.UpdateUserRequest;
import com.example.user_service.controller.dto.response.UserResponse;
import com.example.user_service.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserResponse> getAllUsers() {
        log.info("GET /api/users - Получение списка всех пользователей");
        List<UserResponse> users = userService.getAllUsers();
        log.info("GET /api/users - Успешно получено {} пользователей", users.size());
        return users;
    }

    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable Long id) {
        log.info("GET /api/users/{} - Получение пользователя по ID", id);
        UserResponse user = userService.getUserById(id);
        log.info("GET /api/users/{} - Пользователь найден: {}", id, user.getEmail());
        return user;
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        log.info("POST /api/users - Создание пользователя с email: {}", request.getEmail());
        UserResponse response = userService.createUser(request);
        log.info("POST /api/users - Пользователь создан с ID: {}", response.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public UserResponse updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest request) {
        log.info("PUT /api/users/{} - Обновление пользователя", id);
        UserResponse response = userService.updateUser(id, request);
        log.info("PUT /api/users/{} - Пользователь обновлен", id);
        return response;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("DELETE /api/users/{} - Удаление пользователя", id);
        userService.deleteUser(id);
        log.info("DELETE /api/users/{} - Пользователь удален", id);
        return ResponseEntity.noContent().build();
    }
}
