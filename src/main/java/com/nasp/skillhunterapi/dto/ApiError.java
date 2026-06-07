package com.nasp.skillhunterapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "Api error response")
public record ApiError(
    Instant timestamp,
    Integer status,
    String error,
    String message,
    String path
) {
}
