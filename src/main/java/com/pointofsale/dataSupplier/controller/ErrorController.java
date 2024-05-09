package com.pointofsale.dataSupplier.controller;

import com.pointofsale.dataSupplier.dto.response.CommonResponse;
import jakarta.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ErrorController {

    @ExceptionHandler(value = ResponseStatusException.class)
    public ResponseEntity<?> responseStatusException(ResponseStatusException exception) {
        CommonResponse<String> response = CommonResponse.<String>builder()
                .statusCode(exception.getStatusCode().value())
                .errors(exception.getReason())
                .build();
        return ResponseEntity
                .status(exception.getStatusCode())
                .body(response);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<?> ConstrainViolationException(ConstraintViolationException exception) {
        CommonResponse<String> response = CommonResponse.<String>builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .errors(exception.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }
}
