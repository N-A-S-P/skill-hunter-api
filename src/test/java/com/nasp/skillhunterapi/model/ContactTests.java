package com.nasp.skillhunterapi.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.nasp.skillhunterapi.enums.ContactMethodContext;
import com.nasp.skillhunterapi.enums.ContactMethodType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static com.nasp.skillhunterapi.testutils.builder.ContactBuilder.aContact;
import static com.nasp.skillhunterapi.testutils.builder.ContactMethodBuilder.aContactMethod;

public class ContactTests {
    @Test
    @DisplayName("No Parameter constructor should initialize relationships")
    void constructor_noParams() {
        var contact = new Contact();

        assertThat(contact.getContactMethods()).isNotNull();
    }

    @Test
    @DisplayName("Initialization with passed basic fields should initialize relationships")
    void constructor_basicFields() {
        var contact = new Contact("Clark", "Kent", "Reporter");

        assertThat(contact.getFirstName()).isEqualTo("Clark");
        assertThat(contact.getLastName()).isEqualTo("Kent");
        assertThat(contact.getTitle()).isEqualTo("Reporter");
        assertThat(contact.getContactMethods()).isEmpty();
    }

    @Test
    @DisplayName("Initialization with passed contact methods should set relationships")
    void constructor_contactMethods() {
        var emailMethod = new ContactMethod(ContactMethodType.EMAIL, ContactMethodContext.WORK,
                "clark.kent@dailyplanet.com", true);
        var contact = new Contact("Clark", "Kent", "Reporter", List.of(emailMethod));

        assertThat(contact.getContactMethods()).containsExactly(emailMethod);
    }

    @Nested
    @DisplayName("addContactMethod")
    class AddContactMethod {
        private Contact contact;

        @BeforeEach
        void setup() {
            contact = aContact().build();
        }

        @Test
        @DisplayName("should add contact method")
        void happyPath() {
            var contactMethod = aContactMethod().withoutId().build();
            contact.addContactMethod(contactMethod);
            assertThat(contact.getContactMethods()).containsExactly(contactMethod);
        }

        @Test
        @DisplayName("should throw an exception when adding a preferred contact method of the same type")
        void duplicatePreferredContactMethod() {
            var sut = aContact()
                .withContactMethods(aContactMethod().build())
                .build();

            var duplicateMethod = aContactMethod()
            .withoutId()
            .withContext(ContactMethodContext.PERSONAL)
            .withValue("superman@gmail.com")
            .build();

            assertThatThrownBy(() -> sut.addContactMethod(duplicateMethod))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("only one preferred contact method allowed per type");
        }

        @Test
        @DisplayName("should throw exception when contact method is null")
        void nullContactMethod() {
            assertThatThrownBy(() -> contact.addContactMethod(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("contact method must not be null");
        }
    }

    @Nested
    @DisplayName("getContactMethodById")
    class GetContactMethodById {
        @Test
        @DisplayName ("should return contact method when it exists")
        void happyPath() {
            var contactMethod = aContactMethod().build();
            var contact = aContact()
                    .withContactMethods(contactMethod)
                    .build();

            var result = contact.getContactMethodById(contactMethod.getId());

            assertThat(result).contains(contactMethod);
        }

        @Test
        @DisplayName("should return empty Optional when no matching contact method exists")
        void notExists() {
            var contact = aContact().build();

            var result = contact.getContactMethodById(1L);

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("hasPreferredContactMethodOfType")
    class HasPreferredContactMethodOfType {
        @Test
        @DisplayName("should return true when preferred contact method of type exists")
        void exists() {
            var contact = aContact()
                    .withContactMethods(aContactMethod().build())
                    .build();
            
            var result = contact.hasPreferredContactMethodOfType(ContactMethodType.EMAIL);

            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("should return true when preferred contact method of type exists excluding certain ids")
        void exists_excludeIds() {
            var contact = aContact()
                .withContactMethods(aContactMethod().build())
                .build();

            var result = contact.hasPreferredContactMethodOfType(ContactMethodType.EMAIL, 2L);

            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("should return false when no preferred contact method of type exists")
        void notExists() {
                        var contact = aContact()
                    .withContactMethods(aContactMethod().build())
                    .build();
            
            var result = contact.hasPreferredContactMethodOfType(ContactMethodType.PHONE);

            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("should return false when no preferred contact method of type exists excluding certain ids") 
        void notExists_excludeIds() {
                        var contact = aContact()
                .withContactMethods(aContactMethod().build())
                .build();

            var result = contact.hasPreferredContactMethodOfType(ContactMethodType.EMAIL, 1L);

            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("removeContactMethodById")
    class RemoveContactMethodById {
        private Contact contact;
        private ContactMethod contactMethod;

        @BeforeEach
        void setup() {
            contactMethod = aContactMethod().build();
            contact = aContact()
                    .withContactMethods(contactMethod)
                    .build();
        }

        @Test
        @DisplayName("should remove contact method when it exists")
        void happyPath() {
            contact.removeContactMethod(contactMethod.getId());

            assertThat(contact.getContactMethods()).doesNotContain(contactMethod);
            assertThat(contactMethod.getContact()).isNull();
        }

        @Test
        @DisplayName("should do nothing when no matching contact method exists")
        void notExists() {            
            contact.removeContactMethod(999L);

            assertThat(contact.getContactMethods()).containsExactly(contactMethod);
            assertThat(contactMethod.getContact()).isEqualTo(contact);
        }
    }

    @Nested
    @DisplayName("removeRelationships")
    class RemoveRelationships {
        @Test
        @DisplayName("should remove all relationships")
        void happyPath() {
            var contactMethod = aContactMethod().build();
            var contact = aContact().withContactMethods(contactMethod).build();

            contact.removeRelationships();

            assertThat(contact.getContactMethods()).isEmpty();
            assertThat(contactMethod.getContact()).isNull();
        }
    }
}
