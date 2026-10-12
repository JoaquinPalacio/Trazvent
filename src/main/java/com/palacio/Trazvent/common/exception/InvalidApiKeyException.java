package com.palacio.Trazvent.common.exception;

public class InvalidApiKeyException extends RuntimeException {
    private final ApiKeyError error;

        public InvalidApiKeyException(ApiKeyError error) {
            super(error.getMessage());
            this.error = error;
        }

        public ApiKeyError getError() {
            return error;
        }
    }
