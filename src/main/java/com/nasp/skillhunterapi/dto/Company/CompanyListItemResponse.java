package com.nasp.skillhunterapi.dto.Company;

import com.nasp.skillhunterapi.dto.LookupResponse;

import java.util.List;

public record CompanyListItemResponse(
        Long id,
        String name,
        String website,
        String industry,
        List<LookupResponse> companyTypes
) {
    public CompanyListItemResponse {
        companyTypes = companyTypes == null ?
                List.of() : companyTypes;
    }
}
