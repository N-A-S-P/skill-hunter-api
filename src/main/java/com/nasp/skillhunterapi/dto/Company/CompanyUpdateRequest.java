package com.nasp.skillhunterapi.dto.Company;

import com.nasp.skillhunterapi.enums.CompanyType;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record CompanyUpdateRequest(
        @NotBlank String name,
        String website,
        String industry,
        Set<CompanyType> companyTypes
) {
    public CompanyUpdateRequest {
        companyTypes = companyTypes == null ?
                Set.of() : companyTypes;
    }
}
