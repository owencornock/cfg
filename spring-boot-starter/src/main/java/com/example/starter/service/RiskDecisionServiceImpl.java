package com.example.starter.service;

import com.example.starter.dto.RiskDecisionRequest;
import com.example.starter.dto.RiskDecisionResponse;
import com.example.starter.model.RiskDecision;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class RiskDecisionServiceImpl implements RiskDecisionService {

    @Override
    public RiskDecisionResponse evaluate(RiskDecisionRequest request, String correlationId) {
        log.info("Evaluating risk decision for customer: {}, correlationId: {}",
                request.getClient().getCustomerId(), correlationId);

        int companyAgePoints = calculateCompanyAgePoints(request.getBusiness());
        int turnoverPoints = calculateTurnoverPoints(request.getBusiness());
        int ownerAgePoints = calculateOwnerAgePoints(request.getOwners());
        int loanToTurnoverPoints = calculateLoanToTurnoverPoints(
                request.getLoan(), request.getBusiness());

        int totalScore = Math.min(
                companyAgePoints + turnoverPoints + ownerAgePoints + loanToTurnoverPoints,
                100);

        RiskDecision decision = determineDecision(
                totalScore, request.getLoan(), request.getBusiness());

        List<String> reasons = buildReasons(
                companyAgePoints, turnoverPoints, ownerAgePoints, loanToTurnoverPoints);

        RiskDecisionResponse response = RiskDecisionResponse.builder()
                .decisionId(UUID.randomUUID().toString())
                .riskDecision(decision)
                .riskScore(totalScore)
                .evaluatedFactors(RiskDecisionResponse.EvaluatedFactors.builder()
                        .companyAgePoints(companyAgePoints)
                        .turnoverPoints(turnoverPoints)
                        .ownerAgePoints(ownerAgePoints)
                        .loanToTurnoverPoints(loanToTurnoverPoints)
                        .build())
                .reasons(reasons)
                .createdAt(Instant.now())
                .correlationId(correlationId)
                .build();

        log.info("Risk decision complete: score={}, decision={}, correlationId={}",
                totalScore, decision, correlationId);

        return response;
    }

    private int calculateCompanyAgePoints(RiskDecisionRequest.Business business) {
        // TODO: Implement company age scoring.
        //
        // Calculate the company age in full calendar years from dateOfIncorporation to today.
        // Scoring rules:
        //   - Less than 3 years old:  return 40
        //   - 3 years or older:       return 0
        //
        // Hint: Use Period.between(business.getDateOfIncorporation(), LocalDate.now()).getYears()
        return 0;
    }

    private int calculateTurnoverPoints(RiskDecisionRequest.Business business) {
        // TODO: Implement annual turnover scoring.
        //
        // Use business.getAnnualTurnover().getAmount() and apply these rules:
        //   - Less than 100,000:           return 30
        //   - Between 100,000 and 5,000,000: return 10
        //   - Greater than 5,000,000:      return 25
        return 0;
    }

    private int calculateOwnerAgePoints(List<RiskDecisionRequest.Owner> owners) {
        // TODO: Implement owner age scoring based on the YOUNGEST owner.
        //
        // Find the youngest owner (most recent dateOfBirth), calculate their age, then:
        //   - Age 18 to 21:  return 30
        //   - Age 21 to 25:  return 15
        //   - Age 25+:       return 5
        //
        // Hint: Use owners.stream() to find the max dateOfBirth (youngest person),
        //       then use Period.between(dob, LocalDate.now()).getYears()
        return 0;
    }

    private int calculateLoanToTurnoverPoints(
            RiskDecisionRequest.Loan loan, RiskDecisionRequest.Business business) {
        // TODO: Implement loan-to-turnover ratio scoring.
        //
        // Calculate: ratio = loan.getRequestedAmount() / business.getAnnualTurnover().getAmount()
        // Apply the HIGHEST matching threshold only:
        //   - Ratio > 20%:  return 30
        //   - Ratio > 15%:  return 10
        //   - Ratio < 10%:  return 0
        return 0;
    }

    private RiskDecision determineDecision(
            int totalScore, RiskDecisionRequest.Loan loan, RiskDecisionRequest.Business business) {
        // TODO: Implement decision mapping.
        //
        // First check the override:
        //   - If loan-to-turnover ratio > 20%: return DECLINE (regardless of score)
        //
        // Then map score to decision:
        //   - Score 0 to 33 AND ratio < 10%: return APPROVE
        //   - Score 34 to 66:                return REFER
        //   - Score 67 to 100:               return DECLINE
        return RiskDecision.APPROVE;
    }

    private List<String> buildReasons(
            int companyAgePoints, int turnoverPoints, int ownerAgePoints, int loanToTurnoverPoints) {
        // TODO: Add descriptive reason strings based on which factors contributed.
        //
        // Example reasons:
        //   - "Company is less than 3 years old"
        //   - "Annual turnover below 100k"
        //   - "Youngest owner is under 25"
        //   - "Loan-to-turnover ratio exceeds 20%"
        return new ArrayList<>();
    }
}
