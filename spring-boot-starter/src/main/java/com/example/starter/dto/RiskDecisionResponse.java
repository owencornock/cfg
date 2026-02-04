package com.example.starter.dto;

import com.example.starter.model.RiskDecision;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RiskDecisionResponse {

    private String decisionId;
    private RiskDecision riskDecision;
    private int riskScore;
    private EvaluatedFactors evaluatedFactors;
    private List<String> reasons;
    private Instant createdAt;
    private String correlationId;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EvaluatedFactors {
        private int companyAgePoints;
        private int turnoverPoints;
        private int ownerAgePoints;
        private int loanToTurnoverPoints;
    }
}
