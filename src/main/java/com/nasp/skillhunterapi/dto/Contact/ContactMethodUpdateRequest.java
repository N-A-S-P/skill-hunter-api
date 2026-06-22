package com.nasp.skillhunterapi.dto.Contact;

import com.nasp.skillhunterapi.enums.ContactMethodContext;
import com.nasp.skillhunterapi.enums.ContactMethodType;
import jakarta.validation.constraints.NotBlank;

public record ContactMethodUpdateRequest(
        Long id,
        @NotBlank ContactMethodType type,
        @NotBlank ContactMethodContext context,
        @NotBlank String value,
        boolean isPreferred
) {
}
