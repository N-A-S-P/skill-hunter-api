package com.nasp.skillhunterapi.dto.Contact;

public record ContactListItemResponse(
        Long id,
        String firstName,
        String lastName,
        String title
) {
}
