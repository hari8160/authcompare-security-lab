package com.authlab.oauthserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * LOCAL LAB OAuth Authorization Server.
 *
 * This is a real, standards-compliant OAuth 2.0 / OpenID Connect Authorization Server
 * (Spring Authorization Server) — not a mocked button. It issues genuine authorization
 * codes and tokens, scoped entirely to localhost for this lab.
 */
@SpringBootApplication
public class OauthServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(OauthServerApplication.class, args);
    }
}
