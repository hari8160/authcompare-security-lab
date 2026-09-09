package com.authlab.security;

import com.authlab.entity.User;
import com.authlab.service.UserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Bridges the LOCAL LAB OAuth Authorization Server's userinfo response into our
 * local `users` / `oauth_accounts` tables. This is where "authorization result"
 * becomes "an app session" — note we never see or store the user's provider password.
 */
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserService userService;

    public CustomOAuth2UserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId(); // "local-lab"
        String subject = oAuth2User.getAttribute("sub");
        String email = oAuth2User.getAttribute("email");
        String preferredUsername = oAuth2User.getAttribute("preferred_username");
        if (preferredUsername == null) preferredUsername = "oauthuser_" + subject;
        if (email == null || email.isBlank()) email = preferredUsername + "@oauth.local";

        User user = userService.findOrCreateFromOAuth(provider, subject, email, preferredUsername);

        Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());
        attributes.put("local_username", user.getUsername());
        attributes.put("local_role", user.getRole().name());

        return new org.springframework.security.oauth2.core.user.DefaultOAuth2User(
                oAuth2User.getAuthorities(), attributes, "sub");
    }
}
