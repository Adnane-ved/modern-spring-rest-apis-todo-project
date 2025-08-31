package com.spring.todos.controller;

import com.spring.todos.request.AuthenticationRequest;
import com.spring.todos.request.RegisterRequest;
import com.spring.todos.response.AuthenticationResponse;
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

    private final AutheticationService authenticationService;


    public AutheticationController(AutheticationService autheticationService) {
        this.authenticationService = autheticationService;
    }

    @Operation(summary = "Register a user", description ="create a new user")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public void register(@Valid @RequestBody RegisterRequest registerRequest) throws Exception {
        authenticationService.register(registerRequest);
    }

    @Operation(summary="loging a user", description = "submit email and passesord")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public AuthenticationResponse login(@Valid @RequestBody AuthenticationRequest authRequest) throws Exception {
        return authenticationService.login(authRequest);
    }

}
