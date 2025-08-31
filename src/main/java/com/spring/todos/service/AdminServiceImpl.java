package com.spring.todos.service;

import com.spring.todos.entity.Authority;
import com.spring.todos.entity.User;
import com.spring.todos.repository.UserRepository;
import com.spring.todos.response.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    public AdminServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public List<UserResponse> getAllUsers() {
        return StreamSupport.stream(userRepository.findAll().spliterator(),false).map(this::convertToUserResponse).toList();
    }

    @Override
    @Transactional
    public UserResponse promoteToAdmin(long userId) {
        Optional<User> user = userRepository.findById(userId);

        if(user.isEmpty() || user.get().getAuthorities().stream().anyMatch(authority -> "ROLE_ADMIN"
                .equals(authority.getAuthority()))){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User does not exist or already an admin");
        }

        List<Authority> authorities = new ArrayList<>();
        authorities.add(new Authority("ROLE_ADMIN"));
        authorities.add(new Authority("ROLE_EMPLOYEE"));
        user.get().setAuthorities(authorities);

        User savedUser = userRepository.save(user.get());
        return convertToUserResponse(savedUser);
    }

    @Override
    public void deleteNonAdminUser(long userId) {
        Optional<User> user = userRepository.findById(userId);

        if(user.isEmpty() || user.get().getAuthorities().stream().anyMatch(authority -> "ROLE_ADMIN"
                .equals(authority.getAuthority()))){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User does not exist or is an admin");
        }

        userRepository.delete(user.get());
    }

    public UserResponse convertToUserResponse(User user){
        return new UserResponse(
                user.getId(),
                user.getFirstName()+" "+user.getLastName(),
                user.getEmail(),
                user.getAuthorities().stream().map(auth->(Authority) auth).toList()
        );
    }
}
