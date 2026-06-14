package com.nasp.skillhunterapi.dto.Company;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class CompanyAddressRequestTest {
    @Test
    @DisplayName("should default to empty lists if passed null")
    void initializeEmptyLists() {
        var sut = new CompanyAddressRequest(null, null, null);

        assertThat(sut.removeAddressIds()).isEmpty();
        assertThat(sut.updateAddresses()).isEmpty();
        assertThat(sut.createAddresses()).isEmpty();
    }

    @Test
    @DisplayName("should set the fields")
    void initializeLists() {
        var createRequest = new AddressCreateRequest("867 Sauropod Ln", "", "Laurasia", "WV", "75309", Set.of());
        var updateRequest = new AddressUpdateRequest(2L, "903 Ceratopsian Rd", "", "Laurasia", "WV", "75309", Set.of());
        var sut = new CompanyAddressRequest(
                List.of(createRequest),
                List.of(updateRequest),
                List.of(1L)
        );

        assertThat(sut.createAddresses()).singleElement().isEqualTo(createRequest);
        assertThat(sut.updateAddresses()).singleElement().isEqualTo(updateRequest);
        assertThat(sut.removeAddressIds()).containsExactly(1L);
    }

    @Nested
    @DisplayName("hasConflictingAddressIds")
    class HasConflictingAddressIds {
        @Test
        @DisplayName("should return false when only updated and removed ids do not overlap")
        void noConflicts() {
            var updateRequest = new AddressUpdateRequest(2L, "903 Ceratopsian Rd", "", "Laurasia", "WV", "75309", Set.of());
            var sut = new CompanyAddressRequest(
                    List.of(),
                    List.of(updateRequest),
                    List.of(1L)
            );

            assertThat(sut.hasConflictingAddressIds()).isFalse();
        }

        @Test
        @DisplayName("should return true when updated and removed ids do overlap")
        void hasConflicts() {
            var updateRequest = new AddressUpdateRequest(1L, "903 Ceratopsian Rd", "", "Laurasia", "WV", "75309", Set.of());
            var sut = new CompanyAddressRequest(
                    List.of(),
                    List.of(updateRequest),
                    List.of(1L)
            );

            assertThat(sut.hasConflictingAddressIds()).isTrue();
        }
    }
}
