package com.nasp.skillhunterapi.dto.Contact;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record ContactCreateRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        String title,
        List<@Valid ContactMethodCreateRequest> contactMethods
) {
    public ContactCreateRequest {
        contactMethods = contactMethods == null ?
                List.of() : contactMethods;
    }

    @AssertFalse(message = "Multiple contact methods of same type marked as preferred")
    public boolean hasDuplicatePreferredContactMethods() {
        var preferredTypes = contactMethods.stream()
                .filter(ContactMethodCreateRequest::isPreferred)
                .map(ContactMethodCreateRequest::type).toList();

        return preferredTypes.stream().distinct()
                .anyMatch(type -> preferredTypes.stream().filter(otherType -> otherType.equals(type)).count() > 1);
    }
}
