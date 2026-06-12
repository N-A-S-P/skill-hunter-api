package com.nasp.skillhunterapi.dto.Company;

import com.nasp.skillhunterapi.dto.LookupResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Company")
public record CompanyDetailResponse(
        Long id,
        @Schema(example = "Vizzini's Army")
        String name,
        @Schema(example = "https://www.inconceivable.com")
        String website,
        @Schema(example = "Mercenaries")
        String industry,
        List<LookupResponse> companyTypes,
        List<AddressResponse> addresses
) {
}
