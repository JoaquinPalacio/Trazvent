package com.palacio.Trazvent.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidApiKeyException.class)
    public ProblemDetail handleInvalidApiKey(InvalidApiKeyException ex) {
        ApiKeyError error = ex.getError();

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(error.getStatus(), error.getMessage());

        problemDetail.setTitle("Authentication Error");
        problemDetail.setProperty("code", error.name());

        return problemDetail;
    }

    @ExceptionHandler(InvalidRegisterException.class)
        public ProblemDetail handleInvalidRegister(InvalidRegisterException ex) {
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                ex.getMessage()
            );

            problemDetail.setTitle("Registration Conflict");

            return problemDetail;
        }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationExceptions(MethodArgumentNotValidException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST,
            "One or more validation errors occurred."
        );

        problemDetail.setTitle("Invalid Input Data");

        Map<String, String> invalidFields = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError ->
            invalidFields.put(fieldError.getField(), fieldError.getDefaultMessage())
        );

        problemDetail.setProperty("invalidFields", invalidFields);

        return problemDetail;
    }
}
