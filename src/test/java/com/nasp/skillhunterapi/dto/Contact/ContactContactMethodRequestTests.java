package com.nasp.skillhunterapi.dto.Contact;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.nasp.skillhunterapi.enums.ContactMethodContext;
import com.nasp.skillhunterapi.enums.ContactMethodType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ContactContactMethodRequestTests {
    @Test
    @DisplayName("should default to empty lists if passed null")
    void initializeEmptyLists() {
        var sut = new ContactContactMethodRequest(null, null, null);

        assertThat(sut.removeContactMethodIds()).isEmpty();
        assertThat(sut.updateContactMethods()).isEmpty();
        assertThat(sut.createContactMethods()).isEmpty();
    }

    @Test
    @DisplayName("should set the fields")
    void initializeLists() {
        var createRequest = new ContactMethodCreateRequest(ContactMethodType.EMAIL, ContactMethodContext.PERSONAL,
                "test@example.com", false);
        var updateRequest = new ContactMethodUpdateRequest(2L, ContactMethodType.PHONE, ContactMethodContext.WORK,
                "555-1234", true);
        var sut = new ContactContactMethodRequest(
                List.of(createRequest),
                List.of(updateRequest),
                List.of(1L));

        assertThat(sut.removeContactMethodIds()).containsExactly(1L);
        assertThat(sut.updateContactMethods()).containsExactly(updateRequest);
        assertThat(sut.createContactMethods()).containsExactly(createRequest);
    }

    @Nested
    @DisplayName("hasConflictingContactMethodIds")
    class HasConflictingContactMethodIds {
        @Test
        @DisplayName("should return false when only updated and removed ids do not overlap")
        void noConflicts() {
            var updateRequest = new ContactMethodUpdateRequest(2L, ContactMethodType.PHONE, ContactMethodContext.WORK,
                    "555-1234", true);
            var sut = new ContactContactMethodRequest(
                    List.of(),
                    List.of(updateRequest),
                    List.of(1L));

            assertThat(sut.hasConflictingContactMethodIds()).isFalse();
        }

        @Test
        @DisplayName("should return true when updated and removed ids do overlap")
        void hasConflicts() {
            var updateRequest = new ContactMethodUpdateRequest(1L, ContactMethodType.PHONE, ContactMethodContext.WORK,
                    "555-1234", true);
            var sut = new ContactContactMethodRequest(
                    List.of(),
                    List.of(updateRequest),
                    List.of(1L));

            assertThat(sut.hasConflictingContactMethodIds()).isTrue();
        }
    }

    @Nested
    @DisplayName("hasDuplicatePreferredContactMethods")
    class HasDuplicatePreferredContactMethods {
        @Test
        @DisplayName("should return false when no preferred contact methods are duplicated")
        void noDuplicates() {
            var createRequest = new ContactMethodCreateRequest(ContactMethodType.PHONE, ContactMethodContext.WORK,
                    "555-1234", true);
            var updateRequest = new ContactMethodUpdateRequest(2L, ContactMethodType.EMAIL,
                    ContactMethodContext.PERSONAL, "test@example.com", true);
            var sut = new ContactContactMethodRequest(
                    List.of(createRequest),
                    List.of(updateRequest),
                    List.of());

            assertThat(sut.hasDuplicatePreferredContactMethods()).isFalse();
        }

        @Test
        @DisplayName("should return true when two create requests have the same contact method type and are preferred")
        void hasCreateDuplicates() {
            var sut = new ContactContactMethodRequest(
                    List.of(
                            new ContactMethodCreateRequest(ContactMethodType.PHONE, ContactMethodContext.WORK,
                                    "867-5309", true),
                            new ContactMethodCreateRequest(ContactMethodType.PHONE, ContactMethodContext.PERSONAL,
                                    "555-1234", true)),
                    List.of(),
                    List.of());

            assertThat(sut.hasDuplicatePreferredContactMethods()).isTrue();
        }

        @Test
        @DisplayName("should return true when two update requests have the same contact method type and are preferred")
        void hasUpdateDuplicates() {
            var sut = new ContactContactMethodRequest(
                    List.of(),
                    List.of(
                            new ContactMethodUpdateRequest(1L, ContactMethodType.EMAIL, ContactMethodContext.WORK,
                                    "clark.kent@dailyplanet.com", true),
                            new ContactMethodUpdateRequest(2L, ContactMethodType.EMAIL, ContactMethodContext.PERSONAL,
                                    "superman@gmail.com", true)),
                    List.of());

            assertThat(sut.hasDuplicatePreferredContactMethods()).isTrue();
        }

        @Test
        @DisplayName("should return true when a create and update request have the same contact method type and are preferred")
        void hasCreateUpdateDuplicates() {
            var sut = new ContactContactMethodRequest(
                    List.of(new ContactMethodCreateRequest(ContactMethodType.EMAIL, ContactMethodContext.WORK,
                            "clark.kent@dailyplanet.com", true)),
                    List.of(new ContactMethodUpdateRequest(1L, ContactMethodType.EMAIL, ContactMethodContext.PERSONAL,
                            "superman@gmail.com", true)),
                    List.of());

            assertThat(sut.hasDuplicatePreferredContactMethods()).isTrue();
        }
    }
}
