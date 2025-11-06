package com.example.user_service.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;

@Schema(description = "Ответ с данными пользователя")
@Relation(collectionRelation = "users", itemRelation = "user")
public class UserResponse extends RepresentationModel<UserResponse> {

    @Schema(description = "ID пользователя", example = "1")
    private Long id;

    @Schema(description = "Имя пользователя", example = "Иван Иванов")
    private String name;

    @Schema(description = "Email пользователя", example = "user@example.com")
    private String email;

    @Schema(description = "Возраст пользователя", example = "25")
    private Integer age;

    @Schema(description = "Время создания пользователя", example = "2025-01-15T10:30:00")
    private LocalDateTime created_at;

    public UserResponse() {
    }

    public UserResponse(Long id, String name, String email, Integer age, LocalDateTime created_at) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.created_at = created_at;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", created_at=" + created_at +
                '}';
    }
}