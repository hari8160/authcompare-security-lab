package com.authlab.repository;

import com.authlab.entity.OAuthAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OAuthAccountRepository extends JpaRepository<OAuthAccount, Long> {
    Optional<OAuthAccount> findByProviderAndProviderSubject(String provider, String providerSubject);
    boolean existsByUserId(Long userId);
}
