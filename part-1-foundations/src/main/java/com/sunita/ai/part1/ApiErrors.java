package com.sunita.ai.part1;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestControllerAdvice
class ApiErrors {
    @ExceptionHandler({IllegalArgumentException.class, MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponse badRequest(Exception ex) {
        return new ErrorResponse("INVALID_REQUEST", ex.getMessage(), Instant.now());
    }
}

record ErrorResponse(String code, String message, Instant timestamp) {}

