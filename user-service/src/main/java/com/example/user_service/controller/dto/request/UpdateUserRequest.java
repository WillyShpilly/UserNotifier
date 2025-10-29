package com.example.user_service.controller.dto.request;

import jakarta.validation.constraints.*;

public class UpdateUserRequest {
    @Size(min = 2, max = 50, message = "Имя должно быть от 2 до 50 символов")
    private String name;

    @Email(message = "Email должен быть валидным")
    private String email;

    @Min(value = 0, message = "Возраст должен быть положительным числом")
    @Max(value = 150, message = "Возраст должен быть реалистичным")
    private Integer age;

    public UpdateUserRequest() {
    }

    public UpdateUserRequest(String name, String email, Integer age) {
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
        return "UpdateUserRequest{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                '}';
    }
}