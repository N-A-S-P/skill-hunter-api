package com.nasp.skillhunterapi.dto.Company;

import com.nasp.skillhunterapi.dto.LookupResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Company Address")
public record AddressResponse(
        Long id,
        @Schema(example = "123 Any St")
        String line1,
        String line2,
        @Schema(example = "Anytown")
        String city,
        @Schema(example = "AK")
        String state,
        @Schema(example = "99999")
        String postalCode,
        List<LookupResponse> addressTypes
) {
}
