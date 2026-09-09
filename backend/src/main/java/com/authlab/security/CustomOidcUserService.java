package com.authlab.security;

import com.authlab.entity.User;
import com.authlab.service.UserService;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CustomOidcUserService extends OidcUserService {

    private final UserService userService;

    public CustomOidcUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest request) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(request);
        String provider = request.getClientRegistration().getRegistrationId();
        String subject = oidcUser.getSubject();
        String preferredUsername = oidcUser.getPreferredUsername();
        if (preferredUsername == null || preferredUsername.isBlank()) preferredUsername = "oauthuser_" + subject;
        String email = oidcUser.getEmail();
        if (email == null || email.isBlank()) email = preferredUsername + "@oauth.local";

        User user = userService.findOrCreateFromOAuth(provider, subject, email, preferredUsername);
        Map<String, Object> claims = new HashMap<>(oidcUser.getClaims());
        claims.put("local_username", user.getUsername());
        claims.put("local_role", user.getRole().name());

        return new DefaultOidcUser(oidcUser.getAuthorities(), oidcUser.getIdToken(),
                new OidcUserInfo(claims), "sub");
    }
}
