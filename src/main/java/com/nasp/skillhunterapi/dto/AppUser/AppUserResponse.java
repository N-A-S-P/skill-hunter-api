package com.nasp.skillhunterapi.dto.AppUser;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "App User")
public record AppUserResponse(
    Long id,
    @Schema(example = "giant")
    String userName,
    @Schema(example = "Fezzik")
    String display
) {
}
