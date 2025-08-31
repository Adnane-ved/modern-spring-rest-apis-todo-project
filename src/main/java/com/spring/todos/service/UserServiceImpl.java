package com.spring.todos.service;

import com.spring.todos.entity.Authority;
import com.spring.todos.entity.User;
import com.spring.todos.repository.UserRepository;
import com.spring.todos.request.PasswordUpdateRequest;
import com.spring.todos.response.UserResponse;
import com.spring.todos.util.FindAuthenticatedUser;
import com.spring.todos.util.FindAuthenticatedUserImpl;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserServiceImpl implements UserService  {

    private final UserRepository userRepository;
    private final FindAuthenticatedUser findAuthenticatedUser;
    private final FindAuthenticatedUserImpl findAuthenticatedUserImpl;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, FindAuthenticatedUser findAuthenticatedUser, FindAuthenticatedUserImpl findAuthenticatedUserImpl, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.findAuthenticatedUser = findAuthenticatedUser;
        this.findAuthenticatedUserImpl = findAuthenticatedUserImpl;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserInfo() {
        User user = findAuthenticatedUserImpl.getAutheticatedUser();

        return new UserResponse(
                user.getId(),
                user.getFirstName()+" "+user.getLastName(),
                user.getEmail(),
                user.getAuthorities().stream().map(auth->(Authority)auth).toList()
        );
    }

    @Override
    public void deleteUser() {
        User user = findAuthenticatedUserImpl.getAutheticatedUser();

        if(isLastAdmin(user)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Admin cannot delete itself");
        }

        userRepository.delete(user);

    }

    @Override
    public void updatePassword(PasswordUpdateRequest passwordUpdateRequest) {
     User user =  findAuthenticatedUserImpl.getAutheticatedUser();

        if(!isOldPasswordCorrect(user.getPassword(), passwordUpdateRequest.getOldPassword())){
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST   ,"Current password is incorrect");
         }

        if(!isNewPasswordConfirmed(passwordUpdateRequest.getNewPassword(), passwordUpdateRequest.getNewPassword2())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST   ,"New password do not match");
         }
        if(!isNewPasswordDifferent(passwordUpdateRequest.getNewPassword(), passwordUpdateRequest.getNewPassword())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST   ,"Old and new passwords must be different");
        }

        user.setPassword(passwordEncoder.encode(passwordUpdateRequest.getNewPassword()));

        userRepository.save(user);

    }

    private boolean isOldPasswordCorrect(String currentPassword, String oldPassword) {
        return passwordEncoder.matches(currentPassword, oldPassword);
    }

    private boolean isNewPasswordConfirmed(String newPassword, String newPasswordConfirmation) {
        return newPassword.equals(newPasswordConfirmation);
    }

    private boolean isNewPasswordDifferent(String oldPassword, String newPasswordConfirmation) {
        return  !oldPassword.equals(newPasswordConfirmation);
    }

    private boolean isLastAdmin(User user){
        boolean isAdmin = user.getAuthorities().stream()
                .anyMatch(a->a.getAuthority()
                        .equals("ROLE_ADMIN"));

        if(isAdmin){
            long adminCount = userRepository.countAdminUsers();
            return adminCount <= 1;
        }

        return false;
    }

}
