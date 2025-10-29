package com.example.user_service.controller.dto.request;

import jakarta.validation.constraints.*;

public class CreateUserRequest {
    @NotBlank(message = "Имя обязательно для заполнения")
    @Size(min = 2, max = 50, message = "Имя должно быть от 2 до 50 символов")
    private String name;

    @NotBlank(message = "Email обязателен для заполнения")
    @Email(message = "Email должен быть валидным")
    private String email;

    @NotNull(message = "Возраст обязателен для заполнения")
    @Min(value = 0, message = "Возраст должен быть положительным числом")
    @Max(value = 150, message = "Возраст должен быть реалистичным")
    private Integer age;

    public CreateUserRequest() {
    }

    public CreateUserRequest(String name, String email, Integer age) {
        this.name = name;
        this.email = email;
        this.age = age;
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

    @Override
    public String toString() {
        return "CreateUserRequest{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                '}';
    }
}