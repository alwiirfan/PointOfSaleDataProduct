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
                .message(null)
                .errors(exception.getMessage())
                .build();
        return ResponseEntity
                .status(exception.getStatusCode())
                .body(response);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<?> ConstrainViolationException(ResponseStatusException exception) {
        CommonResponse<String> response = CommonResponse.<String>builder()
                .message(null)
                .errors(exception.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }
}
