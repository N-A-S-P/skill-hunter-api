package com.nasp.skillhunterapi.dto.Profile;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "App User")
public record ProfileResponse(
    Long id,
    @Schema(example = "giant")
    String userName,
    @Schema(example = "Fezzik")
    String display,
    String givenName,
    String familyName,
    String email
) {
}
