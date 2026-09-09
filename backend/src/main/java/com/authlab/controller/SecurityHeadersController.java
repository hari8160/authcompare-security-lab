package com.authlab.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/security")
@Tag(name = "Security Headers", description = "Introspection endpoint backing the /security-headers page")
public class SecurityHeadersController {

    @GetMapping("/headers")
    @Operation(summary = "Describe the security headers this backend sets, for the frontend to display")
    public Map<String, String> headers() {
        // Actual header values are applied globally by SecurityConfig; this endpoint
        // just gives the frontend human-readable descriptions to render alongside them.
        return Map.of(
            "Content-Security-Policy", "default-src 'self'; frame-ancestors 'none' — restricts resource origins and framing",
            "X-Content-Type-Options", "nosniff — prevents MIME-sniffing away from the declared Content-Type",
            "X-Frame-Options", "DENY — prevents this app being embedded in a clickjacking iframe",
            "Referrer-Policy", "no-referrer — avoids leaking full URLs (which may contain tokens) to other sites",
            "Permissions-Policy", "geolocation=(), microphone=(), camera=() — disables unused browser features",
            "Strict-Transport-Security", "forces HTTPS for future requests once TLS is enabled (see /tls)"
        );
    }
}
