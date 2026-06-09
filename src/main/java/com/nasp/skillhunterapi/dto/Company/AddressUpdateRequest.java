package com.nasp.skillhunterapi.dto.Company;

import com.nasp.skillhunterapi.enums.AddressType;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record AddressUpdateRequest(
        Long id,
        @NotBlank
        String line1,
        String line2,
        @NotBlank
        String city,
        @NotBlank
        String state,
        @NotBlank
        String postalCode,
        Set<AddressType> addressTypes
) {
    public AddressUpdateRequest {
        addressTypes = addressTypes == null ? Set.of() : addressTypes;
    }
}
