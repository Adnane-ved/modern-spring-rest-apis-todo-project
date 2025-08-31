package com.spring.todos.service;

import com.spring.todos.request.AuthenticationRequest;
import com.spring.todos.request.RegisterRequest;
import com.spring.todos.response.AuthenticationResponse;

public interface AutheticationService {

    void register(RegisterRequest input) throws Exception;
    AuthenticationResponse login(AuthenticationRequest input) throws Exception;

}
