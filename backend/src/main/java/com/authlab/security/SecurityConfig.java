package com.authlab.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestCustomizers;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomOidcUserService customOidcUserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final String frontendOrigin;

    public SecurityConfig(
            JwtAuthFilter jwtAuthFilter,
            CustomOAuth2UserService customOAuth2UserService,
            CustomOidcUserService customOidcUserService,
            OAuth2SuccessHandler oAuth2SuccessHandler,
            ClientRegistrationRepository clientRegistrationRepository,
            @Value("${app.cors.allowed-origin}") String frontendOrigin) {

        this.jwtAuthFilter = jwtAuthFilter;
        this.customOAuth2UserService = customOAuth2UserService;
        this.customOidcUserService = customOidcUserService;
        this.oAuth2SuccessHandler = oAuth2SuccessHandler;
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.frontendOrigin = frontendOrigin;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        DefaultOAuth2AuthorizationRequestResolver authorizationRequestResolver =
                new DefaultOAuth2AuthorizationRequestResolver(
                        clientRegistrationRepository,
                        "/oauth2/authorization"
                );

        authorizationRequestResolver.setAuthorizationRequestCustomizer(
                OAuth2AuthorizationRequestCustomizers.withPkce()
        );

        http
            .cors(cors ->
                cors.configurationSource(corsConfigurationSource())
            )

            // Disabled for local Swagger/API testing.
            // The SameSite cookie setting still provides basic CSRF mitigation.
            .csrf(csrf -> csrf.disable())

            // OAuth state and PKCE verifier require a short-lived HTTP session.
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            )

            .headers(headers -> headers
                .contentTypeOptions(contentType -> {
                })
                .contentSecurityPolicy(csp ->
                    csp.policyDirectives(
                        "default-src 'self'; frame-ancestors 'none'"
                    )
                )
                .frameOptions(frame -> frame.deny())
                .referrerPolicy(referrer ->
                    referrer.policy(
                        ReferrerPolicyHeaderWriter.ReferrerPolicy.NO_REFERRER
                    )
                )
                .httpStrictTransportSecurity(hsts ->
                    hsts
                        .includeSubDomains(true)
                        .maxAgeInSeconds(31536000)
                )

                // Keep permissionsPolicy last because this Spring Security
                // version returns PermissionsPolicyConfig from this method.
                .permissionsPolicy(permissions ->
                    permissions.policy(
                        "geolocation=(), microphone=(), camera=()"
                    )
                )
            )

            .exceptionHandling(exceptions -> exceptions
                .defaultAuthenticationEntryPointFor(
                    (request, response, exception) ->
                        response.sendError(
                            HttpServletResponse.SC_UNAUTHORIZED
                        ),
                    new AntPathRequestMatcher("/api/**")
                )
                .defaultAccessDeniedHandlerFor(
                    (request, response, exception) ->
                        response.sendError(
                            HttpServletResponse.SC_FORBIDDEN
                        ),
                    new AntPathRequestMatcher("/api/**")
                )
            )

            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/api/auth/register",
                    "/api/auth/login"
                ).permitAll()

                .requestMatchers(
                    "/swagger-ui.html",
                    "/swagger-ui/**",
                    "/v3/api-docs/**"
                ).permitAll()

                .requestMatchers(
                    "/api/oauth/**",
                    "/oauth2/**",
                    "/login/oauth2/**"
                ).permitAll()

                .requestMatchers(
                    "/api/security/headers"
                ).permitAll()

                .requestMatchers(
                    "/api/admin/**"
                ).hasRole("ADMIN")

                .anyRequest().authenticated()
            )

            .oauth2Login(oauth2 -> oauth2
                .authorizationEndpoint(endpoint ->
                    endpoint.authorizationRequestResolver(
                        authorizationRequestResolver
                    )
                )
                .userInfoEndpoint(userInfo -> userInfo
                    .userService(customOAuth2UserService)
                    .oidcUserService(customOidcUserService)
                )
                .successHandler(oAuth2SuccessHandler)
                .failureUrl(
                    frontendOrigin + "/oauth/callback?status=failure"
                )
            )

            .addFilterBefore(
                jwtAuthFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }

    private CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of(frontendOrigin));

        config.setAllowedMethods(
            List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")
        );

        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);

        return source;
    }
}