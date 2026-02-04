package com.example.starter.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Logs every request with timing, endpoint, HTTP status, and correlation ID.
 *
 * <p>Important: Never log PII (personal names, dates of birth, addresses).
 * Only log identifiers like customerId and correlationId.
 */
@Slf4j
@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {

    private static final String START_TIME_ATTR = "requestStartTime";
    private static final String CORRELATION_HEADER = "X-Correlation-ID";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute(START_TIME_ATTR, System.currentTimeMillis());

        String correlationId = request.getHeader(CORRELATION_HEADER);
        log.info("Request started: method={}, endpoint={}, correlationId={}",
                request.getMethod(), request.getRequestURI(), correlationId);

        return true;
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        long startTime = (long) request.getAttribute(START_TIME_ATTR);
        long duration = System.currentTimeMillis() - startTime;

        String correlationId = response.getHeader(CORRELATION_HEADER);
        log.info("Request completed: method={}, endpoint={}, status={}, durationMs={}, correlationId={}",
                request.getMethod(), request.getRequestURI(), response.getStatus(), duration, correlationId);
    }
}
