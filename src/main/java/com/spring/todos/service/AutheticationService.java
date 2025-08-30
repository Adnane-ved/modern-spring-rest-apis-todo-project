package com.spring.todos.service;

import com.spring.todos.request.RegisterRequest;

public interface AutheticationService {

    void register(RegisterRequest input) throws Exception;
}
