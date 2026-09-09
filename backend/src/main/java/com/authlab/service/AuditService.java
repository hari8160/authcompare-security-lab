package com.authlab.service;

import com.authlab.entity.AuthMethod;
import com.authlab.entity.AuthenticationEvent;
import com.authlab.entity.EventType;
import com.authlab.repository.AuthenticationEventRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditService {

    private final AuthenticationEventRepository eventRepository;

    public AuditService(AuthenticationEventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public void record(Long userId, AuthMethod method, EventType eventType, boolean success, HttpServletRequest request) {
        AuthenticationEvent event = new AuthenticationEvent();
        event.setUserId(userId);
        event.setMethod(method);
        event.setEventType(eventType);
        event.setSuccess(success);
        if (request != null) {
            event.setIpAddress(request.getRemoteAddr());
            String ua = request.getHeader("User-Agent");
            event.setUserAgent(ua != null && ua.length() > 500 ? ua.substring(0, 500) : ua);
        }
        // NOTE: passwords and tokens are never passed into this method or logged anywhere.
        eventRepository.save(event);
    }

    public List<AuthenticationEvent> recent() {
        return eventRepository.findAll(Sort.by(Sort.Direction.DESC, "timestamp"));
    }
}
