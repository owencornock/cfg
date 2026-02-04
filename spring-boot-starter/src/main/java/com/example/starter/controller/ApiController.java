package com.example.starter.controller;

import com.example.starter.dto.HealthResponse;
import com.example.starter.dto.RiskDecisionRequest;
import com.example.starter.dto.RiskDecisionResponse;
import com.example.starter.service.RiskDecisionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ApiController {

    private final RiskDecisionService riskDecisionService;

    @GetMapping("/health")
    public ResponseEntity<HealthResponse> health() {
        return ResponseEntity.ok(HealthResponse.healthy());
    }

    @PostMapping("/risk-decisions")
    public ResponseEntity<RiskDecisionResponse> createRiskDecision(
            @Valid @RequestBody RiskDecisionRequest request,
            @RequestHeader(value = "X-Correlation-ID", required = false) String correlationId) {

        if (correlationId == null || correlationId.isBlank()) {
            correlationId = UUID.randomUUID().toString();
        }

        RiskDecisionResponse response = riskDecisionService.evaluate(request, correlationId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .header("X-Correlation-ID", response.getCorrelationId())
                .body(response);
    }
}
