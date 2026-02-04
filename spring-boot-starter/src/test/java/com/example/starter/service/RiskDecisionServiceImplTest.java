package com.example.starter.service;

import com.example.starter.dto.RiskDecisionRequest;
import com.example.starter.dto.RiskDecisionResponse;
import com.example.starter.model.RiskDecision;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RiskDecisionServiceImplTest {

    private RiskDecisionService riskDecisionService;

    @BeforeEach
    void setUp() {
        riskDecisionService = new RiskDecisionServiceImpl();
    }

    @Nested
    @DisplayName("evaluate method")
    class EvaluateMethod {

        @Test
        @DisplayName("should return a valid response with all required fields")
        void shouldReturnValidResponse() {
            // given
            RiskDecisionRequest request = buildValidRequest();

            // when
            RiskDecisionResponse response = riskDecisionService.evaluate(request, "test-correlation-id");

            // then
            assertThat(response.getDecisionId()).isNotNull().isNotBlank();
            assertThat(response.getRiskDecision()).isNotNull();
            assertThat(response.getRiskScore()).isBetween(0, 100);
            assertThat(response.getEvaluatedFactors()).isNotNull();
            assertThat(response.getReasons()).isNotNull();
            assertThat(response.getCreatedAt()).isNotNull();
            assertThat(response.getCorrelationId()).isEqualTo("test-correlation-id");
        }

        @Test
        @DisplayName("should include the provided correlation ID in the response")
        void shouldIncludeCorrelationId() {
            // given
            String correlationId = "my-custom-id";
            RiskDecisionRequest request = buildValidRequest();

            // when
            RiskDecisionResponse response = riskDecisionService.evaluate(request, correlationId);

            // then
            assertThat(response.getCorrelationId()).isEqualTo(correlationId);
        }

        @Test
        @DisplayName("should generate a unique decision ID for each call")
        void shouldGenerateUniqueDecisionIds() {
            // given
            RiskDecisionRequest request = buildValidRequest();

            // when
            RiskDecisionResponse first = riskDecisionService.evaluate(request, "id-1");
            RiskDecisionResponse second = riskDecisionService.evaluate(request, "id-2");

            // then
            assertThat(first.getDecisionId()).isNotEqualTo(second.getDecisionId());
        }

        @Test
        @DisplayName("should return evaluated factors in the response")
        void shouldReturnEvaluatedFactors() {
            // given
            RiskDecisionRequest request = buildValidRequest();

            // when
            RiskDecisionResponse response = riskDecisionService.evaluate(request, "test-id");

            // then
            RiskDecisionResponse.EvaluatedFactors factors = response.getEvaluatedFactors();
            assertThat(factors).isNotNull();
            assertThat(factors.getCompanyAgePoints()).isGreaterThanOrEqualTo(0);
            assertThat(factors.getTurnoverPoints()).isGreaterThanOrEqualTo(0);
            assertThat(factors.getOwnerAgePoints()).isGreaterThanOrEqualTo(0);
            assertThat(factors.getLoanToTurnoverPoints()).isGreaterThanOrEqualTo(0);
        }

        // TODO: Add tests for the scoring logic as you implement each calculation method.
    }

    private RiskDecisionRequest buildValidRequest() {
        return new RiskDecisionRequest(
                new RiskDecisionRequest.Client("CUST-001", "GB"),
                new RiskDecisionRequest.Business(
                        "Acme Ltd",
                        "UK",
                        LocalDate.of(2020, 1, 15),
                        new RiskDecisionRequest.AnnualTurnover(250000.00, "GBP")),
                List.of(new RiskDecisionRequest.Owner("Jane Smith", LocalDate.of(1990, 5, 20))),
                new RiskDecisionRequest.Loan(50000.00));
    }
}
