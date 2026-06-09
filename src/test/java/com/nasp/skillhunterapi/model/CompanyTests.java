package com.nasp.skillhunterapi.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CompanyTests {
    @Test
    @DisplayName("No Parameter constructor should initialize relationships")
    void constructor_noParams() {
        var company = new Company();

        assertThat(company.getCompanyTypes()).isNotNull();

        assertThat(company.getAddresses()).isNotNull();
        assertThat(company.getAddresses()).hasSize(0);
    }

    @Nested
    @DisplayName("Initialization with passed addresses")
    class ConstructorWithAddressRelationshipInitialization {
        @Test
        @DisplayName("should set the parent of each address passed in")
        void constructor_addressesIncluded() {
            var address = new Address();
            var company = new Company(
                    "Vizzini's Army",
                    "https://www.inconceivable.com",
                    "Mercenaries",
                    Set.of(),
                    List.of(address));

            assertThat(company.getAddresses()).isNotNull();
            assertThat(company.getAddresses()).hasSize(1);

            assertThat(address.getCompany()).isEqualTo(company);
        }

        @Test
        @DisplayName("should initialize address relationships if not passed any")
        void constructor_addressesNull() {
            var company = new Company(
                    "Vizzini's Army",
                    "https://www.inconceivable.com",
                    "Mercenaries",
                    Set.of(),
                    null);

            assertThat(company.getAddresses()).isNotNull();
            assertThat(company.getAddresses()).hasSize(0);
        }

        @Test
        @DisplayName("should initialize companyTypes to empty set")
        void constructor_companyTypesNull() {
            var company = new Company(
                    "Dread Pirates Succession Advisors",
                    "https://www.dp-successors.com",
                    "Legal",
                    null,
                    List.of()
            );

            assertThat(company.getCompanyTypes()).isNotNull();

            assertThat(company.getCompanyTypes()).isNotNull();
        }
    }

    @Nested
    @DisplayName("addAddress")
    class AddAddress {
        @Test
        @DisplayName("should add an address")
        void addAddress_happyPath() {
            var address = new Address();
            var company = new Company();

            company.addAddress(address);

            assertThat(company.getAddresses()).contains(address);
            assertThat(address.getCompany()).isEqualTo(company);
        }

        @Test
        @DisplayName("should throw exception when address is null")
        void addAddress_nullAddress() {
            var company = new Company();

            assertThatThrownBy(() -> company.addAddress(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("address must not be null");
        }
    }

    @Nested
    @DisplayName("getAddressById")
    class GetAddressById {
        @Test
        @DisplayName("should return address if exists")
        void happyPath() {
            var address = new Address();
            address.setId(1L);

            var company = new Company("name", "", "", Set.of(), List.of(address));

            var result = company.getAddressById(1L);

            assertThat(result).contains(address);
        }

        @Test
        @DisplayName("should return empty Optional if no matching address")
        void notExists() {
            var company = new Company();

            var result = company.getAddressById(1L);

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("removeAddressById")
    class RemoveAddressById {
        @Test
        @DisplayName("should remove address")
        void happyPath() {
            var address = new Address();
            address.setId(1L);
            var company = new Company("Magic Max's Marvelous Market", "", "Retail", Set.of(), List.of(address));

            company.removeAddressById(1L);

            assertThat(company.getAddresses()).doesNotContain(address);
            assertThat(address.getCompany()).isNull();
        }
        @Test
        @DisplayName("should do nothing")
        void noMatch() {
            var address = new Address();
            address.setId(1L);
            var company = new Company("Magic Max's Marvelous Market", "", "Retail", Set.of(), List.of(address));

            company.removeAddressById(2L);

            assertThat(company.getAddresses()).contains(address);
            assertThat(address.getCompany()).isNotNull();
        }
    }
}
