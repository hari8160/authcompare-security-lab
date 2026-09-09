package com.authlab.security;

import com.authlab.entity.AuthMethod;
import com.authlab.entity.EventType;
import com.authlab.service.AuditService;
import com.authlab.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * After the Authorization Code + token exchange succeeds, issue the SAME kind of
 * session cookie the traditional flow uses, so /api/auth/me and route protection
 * work identically regardless of which authentication method was used.
 */
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final AuditService auditService;
    private final String frontendOrigin;
    private final boolean secureCookie;

    public OAuth2SuccessHandler(JwtUtil jwtUtil, UserService userService, AuditService auditService,
                                 @Value("${app.cors.allowed-origin}") String frontendOrigin,
                                 @Value("${app.cookie.secure}") boolean secureCookie) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.auditService = auditService;
        this.frontendOrigin = frontendOrigin;
        this.secureCookie = secureCookie;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                         Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String username = oAuth2User.getAttribute("local_username");
        String role = oAuth2User.getAttribute("local_role");

        String token = jwtUtil.generateToken(username, role);

        Cookie cookie = new Cookie(JwtAuthFilter.COOKIE_NAME, token);
        cookie.setHttpOnly(true);
        cookie.setSecure(secureCookie);
        cookie.setPath("/");
        cookie.setMaxAge(3600);
        cookie.setAttribute("SameSite", "Lax");
        response.addCookie(cookie);

        userService.findByUsername(username).ifPresent(u ->
                auditService.record(u.getId(), AuthMethod.OAUTH, EventType.OAUTH_LOGIN, true, request));

        getRedirectStrategy().sendRedirect(request, response, frontendOrigin + "/oauth/callback?status=success");
    }
}
