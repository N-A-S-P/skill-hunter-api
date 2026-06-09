package com.nasp.skillhunterapi.dto.Company;

import java.util.List;

public record CompanyAddressRequest(
        List<AddressCreateRequest> createAddresses,
        List<AddressUpdateRequest> updateAddresses,
        List<Long> removeAddressIds
) {
    public CompanyAddressRequest {
        createAddresses = createAddresses == null ? List.of() : createAddresses;
        updateAddresses = updateAddresses == null ? List.of() : updateAddresses;
        removeAddressIds = removeAddressIds == null ? List.of() : removeAddressIds;
    }
}
