package com.nasp.skillhunterapi.mapping;

import com.nasp.skillhunterapi.dto.Company.AddressCreateRequest;
import com.nasp.skillhunterapi.dto.Company.AddressResponse;
import com.nasp.skillhunterapi.dto.Company.AddressUpdateRequest;
import com.nasp.skillhunterapi.model.Address;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
@RequiredArgsConstructor
public class AddressMapper implements BaseEntityMapper<Address, AddressResponse, AddressCreateRequest, AddressUpdateRequest> {
    private final LookupMapper lookupMapper;

    @Override
    public AddressResponse toResponse(Address entity) {
        return new AddressResponse(
                entity.getId(),
                entity.getLine1(),
                entity.getLine2(),
                entity.getCity(),
                entity.getState(),
                entity.getZipCode(),
                entity.getAddressTypes().stream().map(lookupMapper::toResponse).toList()
        );
    }

    @Override
    public Address toEntity(AddressCreateRequest request) {
        return new Address(
                request.line1(),
                request.line2(),
                request.city(),
                request.state(),
                request.postalCode(),
                request.addressTypes()
        );
    }

    @Override
    public void updateEntity(Address entity, AddressUpdateRequest request) {
        entity.setLine1(request.line1());
        entity.setLine2(request.line2());
        entity.setCity(request.city());
        entity.setState(request.state());
        entity.setZipCode(request.postalCode());
        entity.setAddressTypes(new HashSet<>(request.addressTypes()));
    }
}
