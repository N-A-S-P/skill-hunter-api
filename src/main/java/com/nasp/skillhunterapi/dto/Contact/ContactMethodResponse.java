package com.nasp.skillhunterapi.dto.Contact;

import com.nasp.skillhunterapi.dto.LookupResponse;

public record ContactMethodResponse(
        Long id,
        LookupResponse type,
        LookupResponse context,
        String value,
        Boolean isPreferred
) {
}
