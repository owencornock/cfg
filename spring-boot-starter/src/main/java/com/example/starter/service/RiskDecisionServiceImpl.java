package com.example.starter.service;

import com.example.starter.dto.RiskDecisionRequest;
import com.example.starter.dto.RiskDecisionResponse;
import com.example.starter.model.RiskDecision;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class RiskDecisionServiceImpl implements RiskDecisionService {

    @Override
    public RiskDecisionResponse evaluate(RiskDecisionRequest request, String correlationId) {
        // TODO: Implement this method.
        //
        // This is the main orchestration method. It should:
        //   1. Call each scoring method to get points for each factor
        //   2. Calculate a total score (capped at 100)
        //   3. Determine the risk decision based on the score
        //   4. Collect the reasons for the decision
        //   5. Build and return a RiskDecisionResponse using the builder pattern
        //
        // The response must include: decisionId (UUID), riskDecision, riskScore,
        // evaluatedFactors, reasons, createdAt, and correlationId.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private int calculateCompanyAgePoints(RiskDecisionRequest.Business business) {
        // TODO: Implement company age scoring.
        //
        // Scoring rules:
        //   - Less than 3 years old:  return 40
        //   - 3 years or older:       return 0
        return 0;
    }

    private int calculateTurnoverPoints(RiskDecisionRequest.Business business) {
        // TODO: Implement annual turnover scoring.
        //
        // Scoring rules:
        //   - Less than 100,000:             return 30
        //   - Between 100,000 and 5,000,000: return 10
        //   - Greater than 5,000,000:        return 25
        return 0;
    }

    private int calculateOwnerAgePoints(List<RiskDecisionRequest.Owner> owners) {
        // TODO: Implement owner age scoring based on the YOUNGEST owner.
        //
        // Scoring rules:
        //   - Age 18 to 21:  return 30
        //   - Age 21 to 25:  return 15
        //   - Age 25+:       return 5
        return 0;
    }

    private int calculateLoanToTurnoverPoints(
            RiskDecisionRequest.Loan loan, RiskDecisionRequest.Business business) {
        // TODO: Implement loan-to-turnover ratio scoring.
        //
        // Scoring rules (apply the HIGHEST matching threshold only):
        //   - Ratio > 20%:  return 30
        //   - Ratio > 15%:  return 10
        //   - Ratio < 10%:  return 0
        return 0;
    }

    private RiskDecision determineDecision(
            int totalScore, RiskDecisionRequest.Loan loan, RiskDecisionRequest.Business business) {
        // TODO: Implement decision mapping.
        //
        // Decision rules:
        //   - If loan-to-turnover ratio > 20%: return DECLINE (regardless of score)
        //   - Score 0 to 33 AND ratio < 10%:   return APPROVE
        //   - Score 34 to 66:                   return REFER
        //   - Score 67 to 100:                  return DECLINE
        return RiskDecision.APPROVE;
    }

    private List<String> buildReasons(
            int companyAgePoints, int turnoverPoints, int ownerAgePoints, int loanToTurnoverPoints) {
        // TODO: Return a list of human-readable reason strings for any factor that scored points.
        return new ArrayList<>();
    }
}
