package com.authlab.service;

import com.authlab.entity.OAuthAccount;
import com.authlab.entity.Role;
import com.authlab.entity.User;
import com.authlab.repository.OAuthAccountRepository;
import com.authlab.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final OAuthAccountRepository oAuthAccountRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                        OAuthAccountRepository oAuthAccountRepository,
                        PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.oAuthAccountRepository = oAuthAccountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User register(String username, String email, String rawPassword) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already taken");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already registered");
        }
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(rawPassword)); // never store plaintext
        user.setRole(Role.USER);
        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean checkPassword(User user, String rawPassword) {
        if (user.getPasswordHash() == null) return false; // OAuth-only account, no local password set
        return passwordEncoder.matches(rawPassword, user.getPasswordHash());
    }

    @Transactional
    public User findOrCreateFromOAuth(String provider, String subject, String email, String preferredUsername) {
        Optional<OAuthAccount> existing = oAuthAccountRepository.findByProviderAndProviderSubject(provider, subject);
        if (existing.isPresent()) {
            return existing.get().getUser();
        }

        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User u = new User();
            u.setEmail(email);
            u.setUsername(uniqueUsername(preferredUsername));
            u.setRole(Role.USER);
            u.setPasswordHash(null); // OAuth-only until/unless they also set a local password
            return userRepository.save(u);
        });

        OAuthAccount account = new OAuthAccount();
        account.setUser(user);
        account.setProvider(provider);
        account.setProviderSubject(subject);
        oAuthAccountRepository.save(account);

        return user;
    }

    private String uniqueUsername(String base) {
        String candidate = base;
        int suffix = 1;
        while (userRepository.existsByUsername(candidate)) {
            candidate = base + suffix++;
        }
        return candidate;
    }

    public String authSourceFor(User user) {
        boolean hasPassword = user.getPasswordHash() != null;
        boolean hasOAuth = oAuthAccountRepository.existsByUserId(user.getId());
        if (hasPassword && hasOAuth) return "BOTH";
        if (hasOAuth) return "OAUTH";
        return "PASSWORD";
    }
}
