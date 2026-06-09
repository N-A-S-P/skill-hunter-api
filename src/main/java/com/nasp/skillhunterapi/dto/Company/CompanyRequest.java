package com.nasp.skillhunterapi.dto.Company;

import com.nasp.skillhunterapi.enums.CompanyType;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.Set;

public record CompanyRequest(
        @NotBlank String name,
        String website,
        String industry,
        Set<CompanyType> companyTypes,
        CompanyAddressRequest addressRequests
) {
    public CompanyRequest {
        companyTypes = companyTypes == null ?
                Set.of() : companyTypes;
        addressRequests = addressRequests == null ?
                new CompanyAddressRequest(List.of(), List.of(), List.of()) : addressRequests;
    }
}
