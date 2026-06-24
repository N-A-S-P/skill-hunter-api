package com.nasp.skillhunterapi.dto.Company;

import com.nasp.skillhunterapi.enums.CompanyType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.Set;

public record CompanyCreateRequest(
        @NotBlank String name,
        String website,
        String industry,
        Set<CompanyType> companyTypes,
        List<@Valid AddressCreateRequest> addresses
) {
    public CompanyCreateRequest {
        companyTypes = companyTypes == null ?
                Set.of() : companyTypes;

        addresses = addresses == null ?
                List.of() : addresses;
    }
}
