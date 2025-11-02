package com.example.user_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

}
feat: add comprehensive logging to UserController

- Implement Russian-language logging for all HTTP endpoints
- Include error handling with detailed error messages
- Configure log levels and patterns in application.yml