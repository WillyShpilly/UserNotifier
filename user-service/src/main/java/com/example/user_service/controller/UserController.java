package com.example.user_service.controller;

import com.example.user_service.assembler.UserAssembler;
import com.example.user_service.controller.dto.request.CreateUserRequest;
import com.example.user_service.controller.dto.request.UpdateUserRequest;
import com.example.user_service.controller.dto.response.UserResponse;
import com.example.user_service.entity.User;
import com.example.user_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserAssembler userAssembler;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService, UserAssembler userAssembler) {
        this.userService = userService;
        this.userAssembler = userAssembler;
    }

    @Operation(
            summary = "Получить всех пользователей",
            description = "Возвращает список всех зарегистрированных пользователей с HATEOAS ссылками"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Список пользователей успешно получен",
            content = @Content(schema = @Schema(implementation = UserResponse.class))
    )
    @GetMapping
    public CollectionModel<UserResponse> getAllUsers() {
        log.info("GET /api/users - Получение списка всех пользователей");

        List<UserResponse> users = userService.getAllUsersEntities().stream()
                .map(userAssembler::toModel)
                .toList();

        CollectionModel<UserResponse> collectionModel = CollectionModel.of(users);

        collectionModel.add(linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel());
        collectionModel.add(linkTo(methodOn(UserController.class).createUser(null)).withRel("create"));

        log.info("GET /api/users - Успешно получено {} пользователей", users.size());
        return collectionModel;
    }

    @Operation(
            summary = "Получить пользователя по ID",
            description = "Возвращает пользователя по указанному идентификатору с HATEOAS ссылками"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Пользователь найден",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable Long id) {
        log.info("GET /api/users/{} - Получение пользователя по ID", id);
        User userEntity = userService.getUserEntityById(id);
        UserResponse response = userAssembler.toModel(userEntity);
        log.info("GET /api/users/{} - Пользователь найден: {}", id, response.getEmail());
        return response;
    }

    @Operation(
            summary = "Создать нового пользователя",
            description = "Создает нового пользователя и отправляет событие USER_CREATED в Kafka"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Пользователь успешно создан",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные данные запроса",
                    content = @Content
            )
    })
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        log.info("POST /api/users - Создание пользователя с email: {}", request.getEmail());
        User createdUser = userService.createUserEntity(request);
        UserResponse response = userAssembler.toModel(createdUser);
        log.info("POST /api/users - Пользователь создан с ID: {}", response.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Обновить пользователя",
            description = "Обновляет данные существующего пользователя"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Пользователь успешно обновлен",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден",
                    content = @Content
            )
    })
    @PutMapping("/{id}")
    public UserResponse updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest request) {
        log.info("PUT /api/users/{} - Обновление пользователя", id);
        User updatedUser = userService.updateUserEntity(id, request);
        UserResponse response = userAssembler.toModel(updatedUser);
        log.info("PUT /api/users/{} - Пользователь обновлен", id);
        return response;
    }

    @Operation(
            summary = "Удалить пользователя",
            description = "Удаляет пользователя и отправляет событие USER_DELETED в Kafka"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Пользователь успешно удален"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("DELETE /api/users/{} - Удаление пользователя", id);
        userService.deleteUser(id);
        log.info("DELETE /api/users/{} - Пользователь удален", id);
        return ResponseEntity.noContent().build();
    }
}