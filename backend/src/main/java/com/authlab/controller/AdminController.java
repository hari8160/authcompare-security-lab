package com.authlab.controller;

import com.authlab.dto.UserResponse;
import com.authlab.repository.UserRepository;
import com.authlab.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin", description = "ADMIN-only endpoints — demonstrates authorization vs authentication")
public class AdminController {

    private final UserRepository userRepository;
    private final UserService userService;

    public AdminController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/users")
    @Operation(summary = "List all users (requires ROLE_ADMIN)")
    public List<UserResponse> listUsers() {
        return userRepository.findAll().stream()
                .map(u -> UserResponse.from(u, userService.authSourceFor(u)))
                .toList();
    }
}
