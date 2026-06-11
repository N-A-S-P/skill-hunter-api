package com.nasp.skillhunterapi.model;

import com.nasp.skillhunterapi.enums.AddressType;
import com.nasp.skillhunterapi.enums.CompanyType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.nasp.skillhunterapi.testutils.TestDataCreator.createCompany;
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
    @DisplayName("Initialization with passed company types")
    class ConstructorWithBasicFields {
        @Test
        @DisplayName("should set companyTypes")
        void constructor_companyTypesNotNull() {
            var company = new Company(
                    "Dread Pirates Succession Advisors",
                    "https://www.dp-successors.com",
                    "Legal",
                    Set.of(CompanyType.HIRER)
            );

            assertThat(company.getCompanyTypes()).hasSize(1);
            assertThat(company.getCompanyTypes()).containsExactly(CompanyType.HIRER);
        }

        @Test
        @DisplayName("should initialize companyTypes to empty set")
        void constructor_companyTypesNull() {
            var company = new Company(
                    "Dread Pirates Succession Advisors",
                    "https://www.dp-successors.com",
                    "Legal",
                    null
            );

            assertThat(company.getCompanyTypes()).isNotNull();
            assertThat(company.getCompanyTypes()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Initialization with passed addresses")
    class ConstructorWithAddresses {
        @Test
        @DisplayName("should set company on passed addresses")
        void happyPath() {
            var company = new Company("Jurassic Park San Diego", "", "Entertainment", Set.of(),
                    List.of(
                            new Address("123 Rexy Blvd", "", "San Diego", "CA", "99999", Set.of())
                    ));

            assertThat(company.getAddresses()).hasSize(1);
            assertThat(company.getAddresses()).extracting(Address::getCompany).allMatch(comp -> comp.equals(company));
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

            var company = createCompany(
                    1L,
                    "Magic Max's Marvelous Market",
                    "",
                    "Retail",
                    Set.of(),
                    List.of(address));

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
            var addresses = new ArrayList<Address>();
            addresses.add(address);

            var company = createCompany(
                    1L,
                    "Magic Max's Marvelous Market",
                    "",
                    "Retail",
                    Set.of(),
                    addresses);

            company.removeAddressById(1L);

            assertThat(company.getAddresses()).doesNotContain(address);
            assertThat(address.getCompany()).isNull();
        }
        @Test
        @DisplayName("should do nothing")
        void noMatch() {
            var address = new Address();
            address.setId(1L);
            var addresses = new ArrayList<Address>();
            addresses.add(address);

            var company = createCompany(
                    1L,
                    "Magic Max's Marvelous Market",
                    "",
                    "Retail",
                    Set.of(),
                    addresses);

            company.removeAddressById(2L);

            assertThat(company.getAddresses()).containsExactly(address);
            assertThat(address.getCompany()).isEqualTo(company);
        }
    }

    @Nested
    @DisplayName("removeRelationships")
    class RemoveRelationships {
        @Test
        @DisplayName("should remove existing relationships")
        void happyPath() {
            var addresses = new ArrayList<Address>();
            addresses.add(new Address("123 Milton Ave", "Ste 100", "Woodmine", "AZ", "99999", Set.of(AddressType.HQ)));
            addresses.get(0).setId(1L);

            var company = createCompany(1L,"Bradley Enterprises, LLC", "", "Manufacturing", Set.of(), addresses);

            company.removeRelationships();

            assertThat(company.getAddresses()).isEmpty();
            assertThat(addresses).extracting(Address::getCompany).allMatch(Objects::isNull);
        }
    }
}
