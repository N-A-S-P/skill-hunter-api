package com.nasp.skillhunterapi.dto.Contact;

import java.util.List;

public record ContactDetailResponse(
        Long id,
        String firstName,
        String lastName,
        String title,
        List<ContactMethodResponse> contactMethods
) {
    public ContactDetailResponse {
        contactMethods = contactMethods == null ?
                List.of() : contactMethods;
    }
}
