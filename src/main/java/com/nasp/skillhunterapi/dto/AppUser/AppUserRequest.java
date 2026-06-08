package com.nasp.skillhunterapi.dto.AppUser;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Create or update an AppUser")
public record AppUserRequest(
    @NotBlank
    @Schema(example = "imontoya")
    String userName,
    @Schema(example = "Inigo Montoya")
    String display
) {
}
