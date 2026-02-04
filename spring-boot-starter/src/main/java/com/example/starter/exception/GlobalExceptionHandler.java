package com.example.starter.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // TODO: Implement centralized error handling for the API.
    //
    // This class extends ResponseEntityExceptionHandler, which gives you a base
    // for handling Spring MVC exceptions. You need to add handlers for:
    //
    //   1. Validation errors (MethodArgumentNotValidException) - return 400 with field-level error details
    //   2. IllegalArgumentException - return 400 with the exception message
    //   3. A catch-all for unexpected exceptions - return 500
    //
    // Spring's ProblemDetail (RFC 7807) is a good response format to use here.
}
