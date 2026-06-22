package com.nasp.skillhunterapi.mapping;

import static com.nasp.skillhunterapi.testutils.LookupResponseAssertions.matchesLookup;
import static com.nasp.skillhunterapi.testutils.builder.ContactBuilder.aContact;
import static com.nasp.skillhunterapi.testutils.builder.ContactMethodBuilder.aContactMethod;
import static com.nasp.skillhunterapi.testutils.builder.ProfileBuilder.aProfile;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.nasp.skillhunterapi.dto.Contact.ContactCreateRequest;
import com.nasp.skillhunterapi.dto.Contact.ContactMethodCreateRequest;
import com.nasp.skillhunterapi.dto.Contact.ContactUpdateRequest;
import com.nasp.skillhunterapi.enums.ContactMethodContext;
import com.nasp.skillhunterapi.enums.ContactMethodType;

public class ContactMapperTests {
    private final ContactMapper sut = new ContactMapper(new ContactMethodMapper(new LookupMapper()));

    @Test
    @DisplayName("should map Contact to ContactDetailResponse")
    void toDetailResponse() {
        var contact = aContact()
                .withContactMethods(aContactMethod().build())
                .build();

        var result = sut.toDetailResponse(contact);

        assertThat(result.id()).isEqualTo(contact.getId());
        assertThat(result.firstName()).isEqualTo(contact.getFirstName());
        assertThat(result.lastName()).isEqualTo(contact.getLastName());
        assertThat(result.title()).isEqualTo(contact.getTitle());
        assertThat(result.contactMethods()).singleElement().satisfies(contactMethod -> {
            var expectedContactMethod = contact.getContactMethods().getFirst();
            assertThat(contactMethod.id()).isEqualTo(expectedContactMethod.getId());
            assertThat(contactMethod.type()).satisfies(matchesLookup(expectedContactMethod.getType()));
            assertThat(contactMethod.context()).satisfies(matchesLookup(expectedContactMethod.getContext()));
            assertThat(contactMethod.value()).isEqualTo(expectedContactMethod.getValue());
            assertThat(contactMethod.isPreferred()).isEqualTo(expectedContactMethod.isPreferred());
        });
    }

    @Test
    @DisplayName("should map Contact to ContactListItemResponse")
    void toListItemResponse() {
        var contact = aContact().build();

        var result = sut.toListItemResponse(contact);

        assertThat(result.id()).isEqualTo(contact.getId());
        assertThat(result.firstName()).isEqualTo(contact.getFirstName());
        assertThat(result.lastName()).isEqualTo(contact.getLastName());
        assertThat(result.title()).isEqualTo(contact.getTitle());
    }

    @Test
    @DisplayName("should map ContactCreateRequest to Contact")
    void toEntity() {
        var profile = aProfile().build();
        var methodRequest = new ContactMethodCreateRequest(ContactMethodType.EMAIL, ContactMethodContext.WORK, "clark.kent@dailyplanet.com", true);
        var request = new ContactCreateRequest("Clark", "Kent", "Reporter", List.of(methodRequest));

        var result = sut.toEntity(request, profile);

        assertThat(result.getFirstName()).isEqualTo(request.firstName());
        assertThat(result.getLastName()).isEqualTo(request.lastName());
        assertThat(result.getTitle()).isEqualTo(request.title());
        assertThat(result.getOwner()).isEqualTo(profile);
        assertThat(result.getContactMethods()).singleElement().satisfies(actual -> {
            assertThat(actual.getType()).isEqualTo(methodRequest.type());
            assertThat(actual.getContext()).isEqualTo(methodRequest.context());
            assertThat(actual.getValue()).isEqualTo(methodRequest.value());
            assertThat(actual.isPreferred()).isEqualTo(methodRequest.isPreferred());
            assertThat(actual.getContact()).isEqualTo(result);
        });
    }

    @Test
    @DisplayName("should update Contact from ContactUpdateRequest")
    void updateEntity() {
        var contact = aContact().build();
        var request = new ContactUpdateRequest("Jimmy", "Olsen", "Photographer");

        sut.updateEntity(contact, request);

        assertThat(contact.getFirstName()).isEqualTo(request.firstName());
        assertThat(contact.getLastName()).isEqualTo(request.lastName());
        assertThat(contact.getTitle()).isEqualTo(request.title());
    }
}
