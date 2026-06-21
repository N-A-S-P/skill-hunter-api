package com.nasp.skillhunterapi.dto.Contact;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.nasp.skillhunterapi.enums.ContactMethodContext;
import com.nasp.skillhunterapi.enums.ContactMethodType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ContactCreateRequestTests {
    @Test
    @DisplayName("should default to empty list if passed null")
    void initializeEmptyList() {
        var sut = new ContactCreateRequest("John", "Doe", "Developer", null);

        assertThat(sut.contactMethods()).isEmpty();
    }

    @Test
    @DisplayName("should set the fields")
    void initializeList() {
        var contactMethod = new ContactMethodCreateRequest(ContactMethodType.EMAIL, ContactMethodContext.PERSONAL,
                "superman@gmail.com", true);
        var sut = new ContactCreateRequest("John", "Doe", "Developer", List.of(contactMethod));

        assertThat(sut.contactMethods()).containsExactly(contactMethod);
    }

    @Nested
    @DisplayName("hasDuplicatePreferredContactMethods")
    class HasDuplicatePreferredContactMethods {
        @Test
        @DisplayName("should return false when no contact methods are preferred")
        void noPreferred() {
            var sut = new ContactCreateRequest("John", "Doe", "Developer",
                    List.of(new ContactMethodCreateRequest(ContactMethodType.EMAIL, ContactMethodContext.PERSONAL,
                            "superman@gmail.com", false),
                            new ContactMethodCreateRequest(ContactMethodType.PHONE, ContactMethodContext.WORK,
                                    "867-5309", true),
                            new ContactMethodCreateRequest(ContactMethodType.EMAIL, ContactMethodContext.WORK,
                                    "clark.kent@dailyplanet.com", false)));

            assertThat(sut.hasDuplicatePreferredContactMethods()).isFalse();
        }

        @Test
        @DisplayName("should return true when two contact methods have the same contact method type and are preferred")
        void hasDuplicates() {
            var sut = new ContactCreateRequest("John", "Doe", "Developer",
                    List.of(new ContactMethodCreateRequest(ContactMethodType.EMAIL, ContactMethodContext.PERSONAL,
                            "superman@gmail.com", true),
                            new ContactMethodCreateRequest(ContactMethodType.EMAIL, ContactMethodContext.WORK,
                                    "clark.kent@dailyplanet.com", true)));

            assertThat(sut.hasDuplicatePreferredContactMethods()).isTrue();
        }
    }
}
