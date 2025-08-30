package com.spring.todos.controller;

import com.spring.todos.request.RegisterRequest;
import com.spring.todos.service.AutheticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name="Authetication REST API Endpoints", description = "operation related to register and login")
public class AutheticationController {

    private final AutheticationService autheticationService;


    public AutheticationController(AutheticationService autheticationService) {
        this.autheticationService = autheticationService;
    }

    @Operation(summary = "Register a user", description ="create a new user")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public void register(@Valid @RequestBody RegisterRequest registerRequest) throws Exception {
        autheticationService.register(registerRequest);
    }

}
