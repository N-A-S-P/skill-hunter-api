package com.nasp.skillhunterapi.dto.Contact;

import jakarta.validation.constraints.NotBlank;

public record ContactUpdateRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        String title
) {
}
