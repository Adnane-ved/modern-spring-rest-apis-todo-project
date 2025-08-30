package com.spring.todos.service;

import com.spring.todos.entity.Authority;
import com.spring.todos.entity.User;
import com.spring.todos.repository.UserRepository;
import com.spring.todos.request.RegisterRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class AuthenticationServiceImpl implements AutheticationService{

   private final UserRepository userRepository;
   private final PasswordEncoder passwordEncoder;

    public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
