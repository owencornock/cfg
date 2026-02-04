package com.example.starter.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiskDecisionRequest {

    @NotNull(message = "Client details are required")
    @Valid
    private Client client;

    @NotNull(message = "Business details are required")
    @Valid
    private Business business;

    @NotEmpty(message = "At least one owner is required")
    @Valid
    private List<Owner> owners;

    @NotNull(message = "Loan details are required")
    @Valid
    private Loan loan;

    // --- Nested request objects ---

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Client {

        @NotBlank(message = "Customer ID is required")
        private String customerId;

        @NotBlank(message = "Country of application is required")
        @Pattern(regexp = "^[A-Z]{2}$", message = "Country of application must be a 2-letter ISO code (e.g. GB)")
        private String countryOfApplication;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Business {

        @NotBlank(message = "Legal name is required")
        private String legalName;

        @NotNull(message = "Country of incorporation is required")
        private String countryOfIncorporation;

        @NotNull(message = "Date of incorporation is required")
        private LocalDate dateOfIncorporation;

        @NotNull(message = "Annual turnover is required")
        @Valid
        private AnnualTurnover annualTurnover;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnnualTurnover {

        @Min(value = 0, message = "Amount must be zero or positive")
        private double amount;

        @NotBlank(message = "Currency is required")
        @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be a 3-letter ISO code (e.g. GBP)")
        private String currency;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Owner {

        @NotBlank(message = "Owner full name is required")
        private String fullName;

        @NotNull(message = "Owner date of birth is required")
        private LocalDate dateOfBirth;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Loan {

        @Min(value = 0, message = "Requested amount must be zero or positive")
        private double requestedAmount;
    }
}
