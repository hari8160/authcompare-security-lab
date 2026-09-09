package com.authlab.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class LoginResponse {
    private final UserResponse user;

    @Schema(description = "JWT for Swagger's Authorize button. The browser frontend uses the HttpOnly cookie instead.")
    private final String accessToken;

    private final String tokenType = "Bearer";

    public LoginResponse(UserResponse user, String accessToken) {
        this.user = user;
        this.accessToken = accessToken;
    }

    public UserResponse getUser() { return user; }
    public String getAccessToken() { return accessToken; }
    public String getTokenType() { return tokenType; }
}
