package com.authlab.repository;

import com.authlab.entity.AuthenticationEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface AuthenticationEventRepository extends JpaRepository<AuthenticationEvent, Long> {
    List<AuthenticationEvent> findAll(Sort sort);
}
