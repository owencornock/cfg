package com.example.starter.service;

import com.example.starter.dto.RiskDecisionRequest;
import com.example.starter.dto.RiskDecisionResponse;

public interface RiskDecisionService {

    RiskDecisionResponse evaluate(RiskDecisionRequest request, String correlationId);
}
