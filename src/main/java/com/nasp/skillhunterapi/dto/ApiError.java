package com.nasp.skillhunterapi.dto;

import java.time.Instant;

public record ApiError(
    Instant timestamp,
    Integer status,
    String error,
    String message,
    String path
) {
}
