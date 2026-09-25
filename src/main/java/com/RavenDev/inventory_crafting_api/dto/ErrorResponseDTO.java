package com.RavenDev.inventory_crafting_api.dto;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponseDTO(
        int status,
        String error,
        String message,
        LocalDateTime timestamp,
        Map< String, String > validationErrors
) {
    public ErrorResponseDTO(int status, String error, String message) {
        this(status, error, message, LocalDateTime.now(), null);
    }

    public ErrorResponseDTO(int status, String error, String message, Map< String, String > validationErrors) {
        this(status, error, message, LocalDateTime.now(), validationErrors);
    }
}