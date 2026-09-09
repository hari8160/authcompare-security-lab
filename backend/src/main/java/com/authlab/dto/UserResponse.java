package com.authlab.dto;

import com.authlab.entity.Role;
import com.authlab.entity.User;

public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private Role role;
    private boolean enabled;
    private String authSource; // "PASSWORD" | "OAUTH" | "BOTH"

    public static UserResponse from(User u, String authSource) {
        UserResponse r = new UserResponse();
        r.id = u.getId();
        r.username = u.getUsername();
        r.email = u.getEmail();
        r.role = u.getRole();
        r.enabled = u.isEnabled();
        r.authSource = authSource;
        return r;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public Role getRole() { return role; }
    public boolean isEnabled() { return enabled; }
    public String getAuthSource() { return authSource; }
}
