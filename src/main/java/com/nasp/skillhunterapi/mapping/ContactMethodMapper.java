package com.nasp.skillhunterapi.mapping;

import com.nasp.skillhunterapi.dto.Contact.ContactMethodCreateRequest;
import com.nasp.skillhunterapi.dto.Contact.ContactMethodResponse;
import com.nasp.skillhunterapi.dto.Contact.ContactMethodUpdateRequest;
import com.nasp.skillhunterapi.model.ContactMethod;
import org.springframework.stereotype.Component;

@Component
public class ContactMethodMapper implements BaseEntityMapper<ContactMethod, ContactMethodResponse, ContactMethodCreateRequest, ContactMethodUpdateRequest> {
    private final LookupMapper lookupMapper;

    public ContactMethodMapper(LookupMapper lookupMapper) {
        this.lookupMapper = lookupMapper;
    }

    @Override
    public ContactMethodResponse toResponse(ContactMethod entity) {
        return new ContactMethodResponse(
                entity.getId(),
                lookupMapper.toResponse(entity.getType()),
                lookupMapper.toResponse(entity.getContext()),
                entity.getValue(),
                entity.isPreferred()
        );
    }

    @Override
    public ContactMethod toEntity(ContactMethodCreateRequest request) {
        return new ContactMethod(
                request.type(),
                request.context(),
                request.value(),
                request.isPreferred()
        );
    }

    @Override
    public void updateEntity(ContactMethod entity, ContactMethodUpdateRequest request) {
        entity.setType(request.type());
        entity.setContext(request.context());
        entity.setValue(request.value());
        entity.setPreferred(request.isPreferred());
    }
}
