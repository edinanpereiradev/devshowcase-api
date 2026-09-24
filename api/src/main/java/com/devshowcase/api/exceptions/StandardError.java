package com.devshowcase.api.exceptions;

import java.time.Instant;
import java.util.Map;

public record StandardError(
        Instant timestamp,
        Integer status,
        String error,
        String message,
        String path,
        Map<String, String> validationErrors
) {
    public StandardError(Instant timestamp, Integer status, String error, String message, String path) {
        this(timestamp, status, error, message, path, null);
    }
}