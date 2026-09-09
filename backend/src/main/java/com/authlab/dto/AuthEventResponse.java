package com.authlab.dto;

import com.authlab.entity.AuthenticationEvent;

import java.time.Instant;

public class AuthEventResponse {
    private Long id;
    private Long userId;
    private String method;
    private String eventType;
    private Instant timestamp;
    private String ipAddress;
    private boolean success;

    public static AuthEventResponse from(AuthenticationEvent e) {
        AuthEventResponse r = new AuthEventResponse();
        r.id = e.getId();
        r.userId = e.getUserId();
        r.method = e.getMethod().name();
        r.eventType = e.getEventType().name();
        r.timestamp = e.getTimestamp();
        r.ipAddress = e.getIpAddress();
        r.success = e.isSuccess();
        return r;
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getMethod() { return method; }
    public String getEventType() { return eventType; }
    public Instant getTimestamp() { return timestamp; }
    public String getIpAddress() { return ipAddress; }
    public boolean isSuccess() { return success; }
}
