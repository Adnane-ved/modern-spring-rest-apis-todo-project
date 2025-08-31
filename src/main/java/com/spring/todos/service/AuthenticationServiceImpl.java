package com.spring.todos.service;

import com.spring.todos.entity.Authority;
import com.spring.todos.entity.User;
import com.spring.todos.repository.UserRepository;
import com.spring.todos.request.AuthenticationRequest;
import com.spring.todos.request.RegisterRequest;
import com.spring.todos.response.AuthenticationResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class AuthenticationServiceImpl implements AutheticationService{

   private final UserRepository userRepository;
   private final PasswordEncoder passwordEncoder;
   private final AuthenticationManager  authenticationManager;
   private final JwtService jwtService;


    public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    @Transactional
    public void register(RegisterRequest input) throws Exception {
        if(isEmailTaken(input.getEmail())) {
            throw new Exception("Email already taken");
        }
        User user = buildNewUser(input);
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthenticationResponse login(AuthenticationRequest request) throws Exception {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        String jwtToken = jwtService.generateToken(new HashMap<>(),user);

        return new  AuthenticationResponse(jwtToken);
    }


    private boolean isEmailTaken(String email){
        return userRepository.findByEmail(email).isPresent();
    }


    private User buildNewUser(RegisterRequest input) throws Exception {
        User user = new User();
        user.setFirstName(input.getFirstName());
        user.setLastName(input.getLastName());
        user.setEmail(input.getEmail());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.setAuthorities(assignAuthorities(user));
        return user;
    }

    private List<Authority> assignAuthorities(User user) {
        boolean isFirstUser = userRepository.count() == 0;
        List<Authority> authorities = new ArrayList<>();
        authorities.add(new Authority("ROLE_EMPLOYEE"));
        if(isFirstUser) {
            authorities.add(new Authority("ROLE_ADMIN"));
        }
        return authorities;

    }



}
