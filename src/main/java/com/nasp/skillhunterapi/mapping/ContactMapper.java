package com.nasp.skillhunterapi.mapping;

import com.nasp.skillhunterapi.dto.Contact.*;
import com.nasp.skillhunterapi.model.Contact;
import com.nasp.skillhunterapi.model.Profile;
import org.springframework.stereotype.Component;

@Component
public class ContactMapper implements OwnedEntityMapper<Contact, ContactDetailResponse, ContactListItemResponse, ContactCreateRequest, ContactUpdateRequest> {
    private final ContactMethodMapper contactMethodMapper;

    public ContactMapper(ContactMethodMapper contactMethodMapper) {
        this.contactMethodMapper = contactMethodMapper;
    }

    @Override
    public ContactDetailResponse toDetailResponse(Contact entity) {
        return new ContactDetailResponse(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getTitle(),
                entity.getContactMethods().stream().map(contactMethodMapper::toResponse).toList()
        );
    }

    @Override
    public ContactListItemResponse toListItemResponse(Contact entity) {
        return new ContactListItemResponse(entity.getId(), entity.getFirstName(), entity.getLastName(), entity.getTitle());
    }

    @Override
    public Contact toEntity(ContactCreateRequest contactRequest, Profile owner) {
        var contact = new Contact(
            contactRequest.firstName(),
            contactRequest.lastName(),
            contactRequest.title(),
            contactRequest.contactMethods().stream().map(contactMethodMapper::toEntity).toList()
        );
        contact.setOwner(owner);

        return contact;
    }

    @Override
    public void updateEntity(Contact entity, ContactUpdateRequest contactRequest) {
        entity.setFirstName(contactRequest.firstName());
        entity.setLastName(contactRequest.lastName());
        entity.setTitle(contactRequest.title());
    }
}
