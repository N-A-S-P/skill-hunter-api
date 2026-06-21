package com.nasp.skillhunterapi.dto.Contact;

import java.util.List;

public record ContactContactMethodRequest(
        List<ContactMethodCreateRequest> createContactMethods,
        List<ContactMethodUpdateRequest> updateContactMethods,
        List<Long> removeContactMethodIds) {
    public ContactContactMethodRequest {
        createContactMethods = createContactMethods == null ? List.of() : createContactMethods;
        updateContactMethods = updateContactMethods == null ? List.of() : updateContactMethods;
        removeContactMethodIds = removeContactMethodIds == null ? List.of() : removeContactMethodIds;
    }

    public Boolean hasConflictingContactMethodIds() {
        return updateContactMethods.stream()
                .map(ContactMethodUpdateRequest::id)
                .anyMatch(removeContactMethodIds::contains);
    }

    public boolean hasDuplicatePreferredContactMethods() {
        var updatePreferred = updateContactMethods.stream().filter(ContactMethodUpdateRequest::isPreferred)
                .map(ContactMethodUpdateRequest::type).toList();
        var createPreferred = createContactMethods.stream().filter(ContactMethodCreateRequest::isPreferred)
                .map(ContactMethodCreateRequest::type).toList();

        var createConflicts = createPreferred.stream().distinct()
                .anyMatch(type -> createPreferred.stream().filter(otherType -> otherType.equals(type)).count() > 1);

        var updateConflicts = updatePreferred.stream().distinct()
                .anyMatch(type -> updatePreferred.stream().filter(otherType -> otherType.equals(type)).count() > 1);

        var createUpdateConflicts = createPreferred.stream().distinct()
                .anyMatch(type -> updatePreferred.stream().filter(otherType -> otherType.equals(type)).count() > 0);

        return createConflicts || updateConflicts || createUpdateConflicts;
    }
}
