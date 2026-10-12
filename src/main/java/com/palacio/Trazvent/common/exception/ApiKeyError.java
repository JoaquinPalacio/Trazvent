package com.palacio.Trazvent.common.exception;

import org.springframework.http.HttpStatus;

public enum ApiKeyError {
    MISSING_KEY("API Key not provided in the headers.", HttpStatus.UNAUTHORIZED),
    INVALID_KEY("Invalid API Key.", HttpStatus.UNAUTHORIZED);

    private final String message;
    private final HttpStatus status;

    ApiKeyError(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
