package com.nasp.skillhunterapi.dto.Contact;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record ContactCreateRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        String title,
        List<ContactMethodCreateRequest> contactMethods
) {
    public ContactCreateRequest {
        contactMethods = contactMethods == null ?
                List.of() : contactMethods;
    }
}
