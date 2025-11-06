package com.example.user_service.assembler;

import com.example.user_service.controller.UserController;
import com.example.user_service.controller.dto.response.UserResponse;
import com.example.user_service.entity.User;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UserAssembler extends RepresentationModelAssemblerSupport<User, UserResponse> {

    public UserAssembler() {
        super(UserController.class, UserResponse.class);
    }

    @Override
    public UserResponse toModel(User entity) {
        UserResponse response = instantiateModel(entity);

        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setEmail(entity.getEmail());
        response.setAge(entity.getAge());

        response.add(linkTo(
                methodOn(UserController.class).getUserById(entity.getId()))
                .withSelfRel());

        response.add(linkTo(
                methodOn(UserController.class).getAllUsers())
                .withRel("users"));

        response.add(linkTo(
                methodOn(UserController.class).updateUser(entity.getId(), null))
                .withRel("update"));

        response.add(linkTo(
                methodOn(UserController.class).deleteUser(entity.getId()))
                .withRel("delete"));

        return response;
    }
}