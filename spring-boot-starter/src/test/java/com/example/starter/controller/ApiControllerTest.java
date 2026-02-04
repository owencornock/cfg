package com.example.starter.controller;

import com.example.starter.dto.RiskDecisionResponse;
import com.example.starter.model.RiskDecision;
import com.example.starter.service.RiskDecisionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ApiController.class)
class ApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RiskDecisionService riskDecisionService;

    private static final String VALID_REQUEST = """
            {
              "client": { "customerId": "CUST-001", "countryOfApplication": "GB" },
              "business": {
                "legalName": "Acme Ltd",
                "countryOfIncorporation": "UK",
                "dateOfIncorporation": "2020-01-15",
                "annualTurnover": { "amount": 250000.00, "currency": "GBP" }
              },
              "owners": [{ "fullName": "Jane Smith", "dateOfBirth": "1990-05-20" }],
              "loan": { "requestedAmount": 50000.00 }
            }
            """;

    @Test
    @DisplayName("GET /api/v1/health should return UP status")
    void healthEndpointShouldReturnUp() throws Exception {
        mockMvc.perform(get("/api/v1/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("UP")))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }

    @Nested
    @DisplayName("POST /api/v1/risk-decisions")
    class CreateRiskDecision {

        @Test
        @DisplayName("should return 201 with risk decision for valid request")
        void shouldReturn201ForValidRequest() throws Exception {
            // given
            RiskDecisionResponse mockResponse = buildMockResponse("test-correlation-id");
            when(riskDecisionService.evaluate(any(), any())).thenReturn(mockResponse);

            // when/then
            mockMvc.perform(post("/api/v1/risk-decisions")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(VALID_REQUEST))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.decisionId", notNullValue()))
                    .andExpect(jsonPath("$.riskDecision", is("APPROVE")))
                    .andExpect(jsonPath("$.riskScore", is(0)))
                    .andExpect(jsonPath("$.evaluatedFactors.companyAgePoints", is(0)))
                    .andExpect(jsonPath("$.correlationId", notNullValue()));
        }

        @Test
        @DisplayName("should return 400 when client is missing")
        void shouldReturn400WhenClientIsMissing() throws Exception {
            String requestWithoutClient = """
                    {
                      "business": {
                        "legalName": "Acme Ltd",
                        "countryOfIncorporation": "UK",
                        "dateOfIncorporation": "2020-01-15",
                        "annualTurnover": { "amount": 250000.00, "currency": "GBP" }
                      },
                      "owners": [{ "fullName": "Jane Smith", "dateOfBirth": "1990-05-20" }],
                      "loan": { "requestedAmount": 50000.00 }
                    }
                    """;

            mockMvc.perform(post("/api/v1/risk-decisions")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestWithoutClient))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.title", is("Validation Error")));
        }

        @Test
        @DisplayName("should return 400 when owners list is empty")
        void shouldReturn400WhenOwnersEmpty() throws Exception {
            String requestWithEmptyOwners = """
                    {
                      "client": { "customerId": "CUST-001", "countryOfApplication": "GB" },
                      "business": {
                        "legalName": "Acme Ltd",
                        "countryOfIncorporation": "UK",
                        "dateOfIncorporation": "2020-01-15",
                        "annualTurnover": { "amount": 250000.00, "currency": "GBP" }
                      },
                      "owners": [],
                      "loan": { "requestedAmount": 50000.00 }
                    }
                    """;

            mockMvc.perform(post("/api/v1/risk-decisions")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestWithEmptyOwners))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.title", is("Validation Error")));
        }

        @Test
        @DisplayName("should echo X-Correlation-ID header when provided")
        void shouldEchoCorrelationId() throws Exception {
            // given
            String correlationId = "my-correlation-id";
            RiskDecisionResponse mockResponse = buildMockResponse(correlationId);
            when(riskDecisionService.evaluate(any(), eq(correlationId))).thenReturn(mockResponse);

            // when/then
            mockMvc.perform(post("/api/v1/risk-decisions")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("X-Correlation-ID", correlationId)
                            .content(VALID_REQUEST))
                    .andExpect(status().isCreated())
                    .andExpect(header().string("X-Correlation-ID", correlationId));
        }

        @Test
        @DisplayName("should generate X-Correlation-ID when not provided")
        void shouldGenerateCorrelationIdWhenMissing() throws Exception {
            // given
            RiskDecisionResponse mockResponse = buildMockResponse("generated-id");
            when(riskDecisionService.evaluate(any(), any())).thenReturn(mockResponse);

            // when/then
            mockMvc.perform(post("/api/v1/risk-decisions")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(VALID_REQUEST))
                    .andExpect(status().isCreated())
                    .andExpect(header().exists("X-Correlation-ID"));
        }
    }

    private RiskDecisionResponse buildMockResponse(String correlationId) {
        return RiskDecisionResponse.builder()
                .decisionId("dec-123")
                .riskDecision(RiskDecision.APPROVE)
                .riskScore(0)
                .evaluatedFactors(RiskDecisionResponse.EvaluatedFactors.builder()
                        .companyAgePoints(0)
                        .turnoverPoints(0)
                        .ownerAgePoints(0)
                        .loanToTurnoverPoints(0)
                        .build())
                .reasons(List.of())
                .createdAt(Instant.now())
                .correlationId(correlationId)
                .build();
    }
}
