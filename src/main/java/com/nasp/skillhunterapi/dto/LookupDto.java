package com.nasp.skillhunterapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Lookup value")
public record LookupDto(
    @Schema(example = "ON_SITE")
    String value,
    @Schema(example = "On-site")
    String display
) {}
