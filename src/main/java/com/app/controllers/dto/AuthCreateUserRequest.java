package com.app.controllers.dto;

import org.springframework.beans.factory.annotation.Autowired;

import com.app.persistence.entities.UserEntity;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record AuthCreateUserRequest(@NotBlank String username, @NotBlank String password, @Valid AuthCreateRoleRequest roleRequest) {


}
