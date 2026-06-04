package com.nasp.skillhunterapi.dto;

import jakarta.validation.constraints.NotBlank;

public record AppUserRequest(
    @NotBlank String userName,
    String display
) {
}
