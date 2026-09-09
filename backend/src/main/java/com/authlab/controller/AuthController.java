package com.authlab.controller;

import com.authlab.dto.LoginRequest;
import com.authlab.dto.LoginResponse;
import com.authlab.dto.ApiError;
import com.authlab.dto.RegisterRequest;
import com.authlab.dto.UserResponse;
import com.authlab.entity.AuthMethod;
import com.authlab.entity.EventType;
import com.authlab.entity.User;
import com.authlab.security.JwtAuthFilter;
import com.authlab.security.JwtUtil;
import com.authlab.service.AuditService;
import com.authlab.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Traditional Authentication", description = "Username/password register, login, logout, profile")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final AuditService auditService;
    private final boolean secureCookie;

    public AuthController(UserService userService, JwtUtil jwtUtil, AuditService auditService,
                          @Value("${app.cookie.secure}") boolean secureCookie) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.auditService = auditService;
        this.secureCookie = secureCookie;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user with username/password")
    @SecurityRequirements
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req, HttpServletRequest request) {
        User user = userService.register(req.getUsername(), req.getEmail(), req.getPassword());
        auditService.record(user.getId(), AuthMethod.PASSWORD, EventType.REGISTER, true, request);
        return ResponseEntity.ok(UserResponse.from(user, "PASSWORD"));
    }

    @PostMapping("/login")
    @Operation(summary = "Login with username/password; sets an HttpOnly session cookie")
    @SecurityRequirements
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {
        var userOpt = userService.findByUsername(req.getUsername());

        if (userOpt.isEmpty() || !userOpt.get().isEnabled()
                || !userService.checkPassword(userOpt.get(), req.getPassword())) {
            Long userId = userOpt.map(User::getId).orElse(null);
            auditService.record(userId, AuthMethod.PASSWORD, EventType.LOGIN_FAILURE, false, request);
            return ResponseEntity.status(401)
                    .body(new ApiError(401, "Unauthorized", "Invalid username or password"));
        }

        User user = userOpt.get();
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());

        Cookie cookie = new Cookie(JwtAuthFilter.COOKIE_NAME, token);
        cookie.setHttpOnly(true);
        cookie.setSecure(secureCookie);
        cookie.setPath("/");
        cookie.setMaxAge(3600);
        cookie.setAttribute("SameSite", "Lax");
        response.addCookie(cookie);

        auditService.record(user.getId(), AuthMethod.PASSWORD, EventType.LOGIN_SUCCESS, true, request);
        return ResponseEntity.ok(new LoginResponse(
                UserResponse.from(user, userService.authSourceFor(user)), token));
    }

    @PostMapping("/logout")
    @Operation(summary = "Clear the session cookie")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response, Authentication auth) {
        Cookie cookie = new Cookie(JwtAuthFilter.COOKIE_NAME, "");
        cookie.setHttpOnly(true);
        cookie.setSecure(secureCookie);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        Long userId = null;
        if (auth != null) {
            userId = userService.findByUsername(auth.getName()).map(User::getId).orElse(null);
        }
        auditService.record(userId, AuthMethod.PASSWORD, EventType.LOGOUT, true, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    @Operation(summary = "Get the currently authenticated user's profile")
    public ResponseEntity<?> me(Authentication auth) {
        if (auth == null) return ResponseEntity.status(401).build();
        return userService.findByUsername(auth.getName())
                .map(u -> ResponseEntity.ok(UserResponse.from(u, userService.authSourceFor(u))))
                .orElse(ResponseEntity.status(404).build());
    }
}
