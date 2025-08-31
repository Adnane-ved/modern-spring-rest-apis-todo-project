package com.spring.todos.service;

import com.spring.todos.request.PasswordUpdateRequest;
import com.spring.todos.response.UserResponse;
import io.jsonwebtoken.security.Password;

public interface UserService {
    UserResponse getUserInfo();
    void deleteUser();
    void updatePassword(PasswordUpdateRequest passwordUpdateRequest);
}


