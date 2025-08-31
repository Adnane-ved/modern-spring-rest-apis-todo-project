package com.spring.todos.controller;

import com.spring.todos.response.UserResponse;
import com.spring.todos.service.AdminService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="Admin REST API Endpoints")
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<UserResponse> getAllUsers() {
        return adminService.getAllUsers();
    }

    @PutMapping("/{userId}/role")
    public UserResponse promoteToAdmin(@PathVariable @Min(1) long userid ){
        return adminService.promoteToAdmin(userid);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable @Min(1) long userid ){
        adminService.deleteNonAdminUser(userid);
    }
}
