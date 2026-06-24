package com.nasp.skillhunterapi.dto.Company;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertFalse;

import java.util.List;

public record CompanyAddressRequest(
        List<@Valid AddressCreateRequest> createAddresses,
        List<@Valid AddressUpdateRequest> updateAddresses,
        List<Long> removeAddressIds
) {
    public CompanyAddressRequest {
        createAddresses = createAddresses == null ? List.of() : createAddresses;
        updateAddresses = updateAddresses == null ? List.of() : updateAddresses;
        removeAddressIds = removeAddressIds == null ? List.of() : removeAddressIds;
    }

    @AssertFalse(message = "Address cannot be removed and updated in the same request")
    public boolean hasConflictingAddressIds() {
        return updateAddresses.stream()
                .map(AddressUpdateRequest::id)
                .anyMatch(removeAddressIds::contains);
    }
}
