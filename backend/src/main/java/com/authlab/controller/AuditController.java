package com.authlab.controller;

import com.authlab.dto.AuthEventResponse;
import com.authlab.service.AuditService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/audit")
@Tag(name = "Audit", description = "Authentication event log (no passwords or tokens ever stored here)")
public class AuditController {

    private final AuditService auditService;

    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    @GetMapping("/events")
    @Operation(summary = "List recent authentication events")
    public List<AuthEventResponse> events() {
        return auditService.recent().stream().map(AuthEventResponse::from).toList();
    }
}
