package com.spring.todos.service;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface JwtService {
    public String extractUsername(String token);
    public Boolean validateToken(String token, UserDetails userDetails);
    public String generateToken(Map<String,Object> claims, UserDetails userDetails);
}
