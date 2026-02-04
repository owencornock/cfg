package com.example.starter.controller;

import com.example.starter.BaseIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ApiControllerIntegrationTest extends BaseIntegrationTest {

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
    @DisplayName("GET /api/v1/health should return health status")
    void healthEndpointIntegrationTest() throws Exception {
        mockMvc.perform(get("/api/v1/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("UP")))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }

    @Test
    @DisplayName("POST /api/v1/risk-decisions should return 201 for valid request")
    void shouldCreateRiskDecisionForValidRequest() throws Exception {
        mockMvc.perform(post("/api/v1/risk-decisions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_REQUEST))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.decisionId", notNullValue()))
                .andExpect(jsonPath("$.riskDecision", is("APPROVE")))
                .andExpect(jsonPath("$.riskScore", is(0)))
                .andExpect(jsonPath("$.evaluatedFactors.companyAgePoints", is(0)))
                .andExpect(jsonPath("$.evaluatedFactors.turnoverPoints", is(0)))
                .andExpect(jsonPath("$.evaluatedFactors.ownerAgePoints", is(0)))
                .andExpect(jsonPath("$.evaluatedFactors.loanToTurnoverPoints", is(0)))
                .andExpect(jsonPath("$.reasons", notNullValue()))
                .andExpect(jsonPath("$.createdAt", notNullValue()))
                .andExpect(jsonPath("$.correlationId", notNullValue()));
    }

    @Test
    @DisplayName("POST /api/v1/risk-decisions should return 400 for invalid request")
    void shouldReturn400ForInvalidRequest() throws Exception {
        String invalidRequest = """
                {
                  "client": { "customerId": "" }
                }
                """;

        mockMvc.perform(post("/api/v1/risk-decisions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title", is("Validation Error")));
    }

    @Test
    @DisplayName("POST /api/v1/risk-decisions should echo X-Correlation-ID header")
    void shouldEchoCorrelationIdHeader() throws Exception {
        String correlationId = "integration-test-id";

        mockMvc.perform(post("/api/v1/risk-decisions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Correlation-ID", correlationId)
                        .content(VALID_REQUEST))
                .andExpect(status().isCreated())
                .andExpect(header().string("X-Correlation-ID", correlationId))
                .andExpect(jsonPath("$.correlationId", is(correlationId)));
    }

    @Test
    @DisplayName("GET /actuator/health should return actuator health")
    void actuatorHealthEndpointTest() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("UP")));
    }
}
