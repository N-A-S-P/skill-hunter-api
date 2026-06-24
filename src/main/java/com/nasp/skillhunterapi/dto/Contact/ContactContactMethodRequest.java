package com.nasp.skillhunterapi.dto.Contact;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertFalse;

import java.util.List;

public record ContactContactMethodRequest(
        List<@Valid ContactMethodCreateRequest> createContactMethods,
        List<@Valid ContactMethodUpdateRequest> updateContactMethods,
        List<Long> removeContactMethodIds) {
    public ContactContactMethodRequest {
        createContactMethods = createContactMethods == null ? List.of() : createContactMethods;
        updateContactMethods = updateContactMethods == null ? List.of() : updateContactMethods;
        removeContactMethodIds = removeContactMethodIds == null ? List.of() : removeContactMethodIds;
    }

    @AssertFalse(message = "Contact method cannot be removed and updated in the same request")
    public boolean hasConflictingContactMethodIds() {
        return updateContactMethods.stream()
                .map(ContactMethodUpdateRequest::id)
                .anyMatch(removeContactMethodIds::contains);
    }

    @AssertFalse(message = "Request cannot have multiple contact methods of same type marked as preferred")
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
                .anyMatch(type -> updatePreferred.stream().anyMatch(otherType -> otherType.equals(type)));

        return createConflicts || updateConflicts || createUpdateConflicts;
    }
}
