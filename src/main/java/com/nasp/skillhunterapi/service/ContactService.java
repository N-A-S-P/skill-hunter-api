package com.nasp.skillhunterapi.service;

import com.nasp.skillhunterapi.provider.CurrentUserProvider;
import org.springframework.stereotype.Service;

import com.nasp.skillhunterapi.dto.Contact.*;
import com.nasp.skillhunterapi.mapping.ContactMapper;
import com.nasp.skillhunterapi.mapping.ContactMethodMapper;
import com.nasp.skillhunterapi.model.Contact;
import com.nasp.skillhunterapi.repository.ContactRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import static com.nasp.skillhunterapi.util.ExceptionMessages.getEntityIdForOwnerNotFoundMessage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactService {
    private final ContactRepository contactRepository;
    private final ContactMapper contactMapper;
    private final ContactMethodMapper contactMethodMapper;
    private final CurrentUserProvider currentUserProvider;

    private Long getCurrentUserId() {
        return currentUserProvider.getCurrentUserId();
    }

    public ContactDetailResponse getContactById(Long id) {
        return contactMapper.toDetailResponse(getContactByIdAndOwnerId(id));
    }

    public List<ContactListItemResponse> getContacts() {
        var currentUserId = getCurrentUserId();
        return contactRepository.findAllByOwnerId(currentUserId)
                .stream()
                .map(contactMapper::toListItemResponse)
                .toList();
    }

    public ContactDetailResponse createContact(ContactCreateRequest request) {
        var entity = contactMapper.toEntity(request, currentUserProvider.getCurrentUser());
        var saved = contactRepository.save(entity);
        return contactMapper.toDetailResponse(saved);
    }

    public ContactDetailResponse updateContact(Long id, ContactUpdateRequest request) {
        var entity = getContactByIdAndOwnerId(id);
        contactMapper.updateEntity(entity, request);

        var saved = contactRepository.save(entity);
        return contactMapper.toDetailResponse(saved);
    }

    public ContactDetailResponse updateContact(Long id, ContactContactMethodRequest request) {
        var entity = getContactByIdAndOwnerId(id);

        var excludeIds = new ArrayList<>(request.updateContactMethods().stream().map(ContactMethodUpdateRequest::id).toList());
        excludeIds.addAll(request.removeContactMethodIds());

        var exclude = excludeIds.toArray(new Long[0]);

        var requestPreferredTypes = new HashSet<>(
                request.updateContactMethods().stream().filter(ContactMethodUpdateRequest::isPreferred)
                        .map(ContactMethodUpdateRequest::type).distinct().toList());
        requestPreferredTypes.addAll(request.createContactMethods().stream()
                .filter(ContactMethodCreateRequest::isPreferred).map(ContactMethodCreateRequest::type).toList());

        requestPreferredTypes.forEach(type -> {
            if (entity.hasPreferredContactMethodOfType(type, exclude)) {
                throw new IllegalArgumentException("Preferred contact method %s already exists on contact %d".formatted(type.getDisplay(), id));
            }
        });

        for (var removeId : request.removeContactMethodIds()) {
            entity.removeContactMethod(removeId);
        }

        for (var updateRequest : request.updateContactMethods()) {
            var existing = entity.getContactMethodById(updateRequest.id())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Contact Method %d does not exist for contact %d".formatted(updateRequest.id(), id)));
            contactMethodMapper.updateEntity(existing, updateRequest);
        }

        for (var createRequest : request.createContactMethods()) {
            entity.addContactMethod(contactMethodMapper.toEntity(createRequest));
        }

        var saved = contactRepository.save(entity);
        return contactMapper.toDetailResponse(saved);
    }

    public void removeContact(Long id) {
        var contact = getContactByIdAndOwnerId(id);
        contact.removeRelationships();
        contactRepository.delete(contact);
    }

    private Contact getContactByIdAndOwnerId(Long id) {
        var currentUserId = getCurrentUserId();
        return contactRepository.findByIdAndOwnerId(id, currentUserId)
                .orElseThrow(() -> new EntityNotFoundException(
                        getEntityIdForOwnerNotFoundMessage(Contact.class, id, currentUserId)));
    }
}
