package com.nasp.skillhunterapi.mapping;

import static com.nasp.skillhunterapi.testutils.LookupResponseAssertions.matchesLookup;
import static com.nasp.skillhunterapi.testutils.builder.ContactMethodBuilder.aContactMethod;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.nasp.skillhunterapi.dto.Contact.ContactMethodCreateRequest;
import com.nasp.skillhunterapi.dto.Contact.ContactMethodUpdateRequest;
import com.nasp.skillhunterapi.enums.ContactMethodContext;
import com.nasp.skillhunterapi.enums.ContactMethodType;

public class ContactMethodMapperTests {
    private final ContactMethodMapper sut = new ContactMethodMapper(new LookupMapper());

    @Test
    @DisplayName("should map ContactMethod to ContactMethodResponse")
    void toResponse() {
        var contactMethod = aContactMethod().build();

        var result = sut.toResponse(contactMethod);

        assertThat(result.id()).isEqualTo(contactMethod.getId());
        assertThat(result.type()).satisfies(matchesLookup(contactMethod.getType()));
        assertThat(result.context()).satisfies(matchesLookup(contactMethod.getContext()));
        assertThat(result.value()).isEqualTo(contactMethod.getValue());
        assertThat(result.isPreferred()).isEqualTo(contactMethod.isPreferred());
    }

    @Test
    @DisplayName("should map ContactMethodCreateRequest to ContactMethod")
    void toEntity() {
        var request = new ContactMethodCreateRequest(ContactMethodType.PHONE, ContactMethodContext.PERSONAL, "867-5309", false);

        var result = sut.toEntity(request);

        assertThat(result.getType()).isEqualTo(request.type());
        assertThat(result.getContext()).isEqualTo(request.context());
        assertThat(result.getValue()).isEqualTo(request.value());
        assertThat(result.isPreferred()).isEqualTo(request.isPreferred());
    }

    @Test
    @DisplayName("should update ContactMethod from ContactMethodUpdateRequest")
    void updateEntity() {
        var contactMethod = aContactMethod().build();
        var request = new ContactMethodUpdateRequest(1L, ContactMethodType.LINKED_IN, ContactMethodContext.PERSONAL, "/in/clark-kent", false);

        sut.updateEntity(contactMethod, request);

        assertThat(contactMethod.getType()).isEqualTo(request.type());
        assertThat(contactMethod.getContext()).isEqualTo(request.context());
        assertThat(contactMethod.getValue()).isEqualTo(request.value());
        assertThat(contactMethod.isPreferred()).isEqualTo(request.isPreferred());
    }
}
