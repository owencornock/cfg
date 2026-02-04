package com.example.starter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthResponse {

    private String status;
    private Instant timestamp;

    public static HealthResponse healthy() {
        return HealthResponse.builder()
                .status("UP")
                .timestamp(Instant.now())
                .build();
    }
}
