package com.nasp.skillhunterapi.model;

import com.nasp.skillhunterapi.enums.AddressType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class AddressTests {
    @Nested
    @DisplayName("Initialization with parameters")
    class ConstructorWithParameters {
        @Test
        @DisplayName("should set addressTypes")
        void happyPath() {
            var address = new Address("123 Washington Ave", "", "Anytown", "AZ", "99999", Set.of(AddressType.HQ));

            assertThat(address.getAddressTypes()).hasSize(1);
        }

        @Test
        @DisplayName("should default addressTypes to empty set")
        void noAddressTypes() {
            var address = new Address("123 Washington Ave", "", "Anytown", "AZ", "99999", null);

            assertThat(address.getAddressTypes()).isNotNull();
            assertThat(address.getAddressTypes()).hasSize(0);
        }
    }
}
