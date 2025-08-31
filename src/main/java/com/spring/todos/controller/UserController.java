package com.spring.todos.controller;

import com.spring.todos.request.PasswordUpdateRequest;
import com.spring.todos.response.UserResponse;
import com.spring.todos.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name="user API Endpoints", description = "Operations related to info about current user")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "user information" , description ="get current user info")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/info")
    public UserResponse getUserInfo(){
        return userService.getUserInfo();
    }

    @Operation(summary = "Delete user" , description ="Delete current user account")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping
    public void deleteUser(){
        userService.deleteUser();
    }

    @Operation(summary = "Password update" , description ="Change user password after verification")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/password")
    public void passwordUpdate(@Valid @RequestBody PasswordUpdateRequest passwordUpdateRequest){
        userService.updatePassword(passwordUpdateRequest);
    }

}
