package com.nasp.skillhunterapi.service;

import com.nasp.skillhunterapi.dto.Company.*;
import com.nasp.skillhunterapi.enums.AddressType;
import com.nasp.skillhunterapi.enums.CompanyType;
import com.nasp.skillhunterapi.mapping.AddressMapper;
import com.nasp.skillhunterapi.mapping.CompanyMapper;
import com.nasp.skillhunterapi.mapping.LookupMapper;
import com.nasp.skillhunterapi.model.Company;
import com.nasp.skillhunterapi.repository.CompanyRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.nasp.skillhunterapi.testutils.builder.CompanyBuilder.aCompany;
import static com.nasp.skillhunterapi.testutils.LookupResponseAssertions.matchesLookup;
import static com.nasp.skillhunterapi.testutils.TestDataCreator.aUser;
import static com.nasp.skillhunterapi.testutils.builder.AddressBuilder.anAddress;
import static com.nasp.skillhunterapi.util.ExceptionMessages.getEntityIdForOwnerNotFoundMessage;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class CompanyServiceTests {
    @Mock
    private CompanyRepository repository;
    @Mock
    private ProfileService profileService;

    private final LookupMapper lookupMapper = new LookupMapper();

    private final AddressMapper addressMapper = new AddressMapper(lookupMapper);

    private final CompanyMapper companyMapper = new CompanyMapper(addressMapper, lookupMapper);

    private CompanyService sut;


    @BeforeEach
    void initialize() {
        sut = new CompanyService(repository, profileService, companyMapper, addressMapper);
    }

    @Nested
    @DisplayName("getCompany")
    class GetCompany {
        @BeforeEach
        void initialize() {
            when(profileService.getCurrentUserId()).thenReturn(1L);
        }

        @Test
        @DisplayName("should return mapped CompanyDetailResponse")
        void happyPath() { // sploot.
            var company = aCompany()
                    .withCompanyTypes(CompanyType.RECRUITING_AGENCY)
                    .withAddresses(anAddress().build())
                    .build();
            when(repository.findByIdAndOwnerId(1L, 1L)).thenReturn(Optional.of(company));

            var result = sut.getCompany(1L);

            assertThat(result).isInstanceOfSatisfying(CompanyDetailResponse.class, res -> {
                assertThat(res.id()).isEqualTo(company.getId());
                assertThat(res.name()).isEqualTo(company.getName());
                assertThat(res.website()).isEqualTo(company.getWebsite());
                assertThat(res.industry()).isEqualTo(company.getIndustry());
                assertThat(res.companyTypes()).singleElement().satisfies(matchesLookup(CompanyType.RECRUITING_AGENCY));
                assertThat(res.addresses()).hasSize(1);
            });
        }

        @Test
        @DisplayName("should throw EntityNotFound when company is not found")
        void companyNotFoundForIdAndUser() {
            when(profileService.getCurrentUserId()).thenReturn(1L);
            when(repository.findByIdAndOwnerId(1L, 1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> sut.getCompany((1L)))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage(getEntityIdForOwnerNotFoundMessage(Company.class, 1L, 1L));
        }
    }

    @Nested
    @DisplayName("getCompanies")
    class GetCompanies {
        @Test
        @DisplayName("should return list of mapped CompanyListItemResponses")
        void happyPath() {
            when(profileService.getCurrentUserId()).thenReturn(1L);
            when(repository.findAllByOwnerId(1L)).thenReturn(List.of(
                    aCompany().build(),
                    aCompany().withId(2L)
                            .withName("Wesayso Corp")
                            .withWebsite("https://www.wesayso.com")
                            .withIndustry("Manufactoring")
                            .withCompanyTypes(CompanyType.HIRER)
                            .build()
            ));

            var result = sut.getCompanies();

            assertThat(result).hasSize(2);
        }
    }

    @Nested
    @DisplayName("createCompany")
    class CreateCompany {
        @Test
        @DisplayName("should create and return a new company")
        void happyPath() {
            when(profileService.getCurrentUser())
                    .thenReturn(aUser());
            var request = new CompanyCreateRequest(
                    "Charlene's Chalet",
                    "https://www.chaletlala.com",
                    "Fashion",
                    Set.of(CompanyType.HIRER),
                    List.of(new AddressCreateRequest("101 Ficus St", "Ste A113", "La Brea", "CA", "90210", Set.of(AddressType.HQ)))
            );

            var result = sut.createCompany(request);

            verify(repository).save(any(Company.class));

            assertThat(result).isNotNull().satisfies(company -> {
                assertThat(company.name()).isEqualTo(request.name());
                assertThat(company.website()).isEqualTo(request.website());
                assertThat(company.industry()).isEqualTo(request.industry());
                assertThat(company.companyTypes()).singleElement().satisfies(matchesLookup(CompanyType.HIRER));
                assertThat(company.addresses()).singleElement().satisfies(address -> {
                    assertThat(address.line1()).isEqualTo("101 Ficus St");
                    assertThat(address.line2()).isEqualTo("Ste A113");
                    assertThat(address.city()).isEqualTo("La Brea");
                    assertThat(address.state()).isEqualTo("CA");
                    assertThat(address.postalCode()).isEqualTo("90210");
                    assertThat(address.addressTypes()).singleElement().satisfies(matchesLookup(AddressType.HQ));
                });
            });
        }
    }

    @Nested
    @DisplayName("updateCompany")
    class UpdateCompany {
        @BeforeEach
        void initialize() {
            when(profileService.getCurrentUserId()).thenReturn(1L);
        }

        @Nested
        @DisplayName("with basic fields only")
        class BasicFields {
            @Test
            @DisplayName("should update existing company")
            void happyPath() {
                var company = aCompany()
                        .withCompanyTypes(CompanyType.RECRUITING_AGENCY)
                        .build();
                when(repository.findByIdAndOwnerId(1L, 1L)).thenReturn(Optional.of(company));
                when(repository.save(any(Company.class)))
                        .thenAnswer(invocation -> invocation.getArgument(0));

                var request = new CompanyUpdateRequest("Myman's Pans", "https://www.myman.com", "Manufacturing", Set.of(CompanyType.HIRER));

                var result = sut.updateCompany(1L, request);

                verify(repository).save(any(Company.class));

                assertThat(result.name()).isEqualTo(request.name());
                assertThat(result.website()).isEqualTo(request.website());
                assertThat(result.industry()).isEqualTo(request.industry());
                assertThat(result.companyTypes()).singleElement().satisfies(matchesLookup(CompanyType.HIRER));
            }

            @Test
            @DisplayName("should throw exception when company does not exist")
            void notFound() {
                when(repository.findByIdAndOwnerId(1L, 1L)).thenReturn(Optional.empty());

                var request = new CompanyUpdateRequest("Myman's Pans", "https://www.myman.com", "Manufacturing", Set.of(CompanyType.HIRER));

                assertThatThrownBy(() -> sut.updateCompany(1L, request))
                        .isInstanceOf(EntityNotFoundException.class)
                        .hasMessage(getEntityIdForOwnerNotFoundMessage(Company.class, 1L, 1L));
            }
        }

        @Nested
        @DisplayName("with addresses")
        class Addresses {
            @Test
            @DisplayName("should delete addresses with the given ids")
            void deleteHappyPath() {
                var company = aCompany()
                        .withAddresses(anAddress().build())
                        .build();
                when(repository.findByIdAndOwnerId(1L, 1L)).thenReturn(Optional.of(company));
                when(repository.save(any(Company.class))).thenAnswer(invocation -> invocation.getArgument(0));

                var request = new CompanyAddressRequest(null, null, List.of(1L));

                var result = sut.updateCompany(1L, request);

                verify(repository).save(any(Company.class));
                assertThat(result.addresses()).isEmpty();
            }

            @Test
            @DisplayName("should update addresses given")
            void updateHappyPath() {
                var company = aCompany()
                        .withAddresses(
                                anAddress().build()
                        ).build();
                when(repository.findByIdAndOwnerId(1L, 1L))
                        .thenReturn(Optional.of(company));
                when(repository.save(any(Company.class)))
                        .thenAnswer(invocation -> invocation.getArgument(0));

                var request = new CompanyAddressRequest(null,
                        List.of(
                                new AddressUpdateRequest(1L, "45 Hadrosaur Ln", "", "Tarpit City", "MN", "90211", Set.of(AddressType.BILLING))
                        ), null);
                var result = sut.updateCompany(1L, request);

                assertThat(result.addresses()).singleElement().satisfies(actual -> {
                    assertThat(actual.line1()).isEqualTo("45 Hadrosaur Ln");
                    assertThat(actual.line2()).isEmpty();
                    assertThat(actual.city()).isEqualTo("Tarpit City");
                    assertThat(actual.state()).isEqualTo("MN");
                    assertThat(actual.postalCode()).isEqualTo("90211");
                    assertThat(actual.addressTypes()).singleElement().satisfies(matchesLookup(AddressType.BILLING));
                });

                verify(repository).save(any(Company.class));
            }

            @Test
            void createHappyPath() {
                when(repository.findByIdAndOwnerId(1L, 1L))
                        .thenReturn(Optional.of(aCompany().build()));
                when(repository.save(any(Company.class)))
                        .thenAnswer(invocation -> invocation.getArgument(0));
                var request = new CompanyAddressRequest(
                        List.of(new AddressCreateRequest("98 Styroforma St", "", "Mugsville", "NY", "10101", Set.of(AddressType.MAILING))),
                        null, null);

                var result = sut.updateCompany(1L, request);

                assertThat(result.addresses()).singleElement().satisfies(actual -> {
                    assertThat(actual.line1()).isEqualTo("98 Styroforma St");
                    assertThat(actual.line2()).isEmpty();
                    assertThat(actual.city()).isEqualTo("Mugsville");
                    assertThat(actual.state()).isEqualTo("NY");
                    assertThat(actual.postalCode()).isEqualTo("10101");
                    assertThat(actual.addressTypes()).singleElement().satisfies(matchesLookup(AddressType.MAILING));
                });

                verify(repository).save(any(Company.class));
            }

            @Test
            @DisplayName("should throw exception when company does not exist")
            void companyNotFound() {
                when(repository.findByIdAndOwnerId(1L, 1L)).thenReturn(Optional.empty());

                assertThatThrownBy(() -> sut.updateCompany(1L, new CompanyAddressRequest(null, null, null)))
                        .isInstanceOf(EntityNotFoundException.class)
                        .hasMessage(getEntityIdForOwnerNotFoundMessage(Company.class, 1L, 1L));
                verify(repository, never()).save(any(Company.class));
            }

            @Test
            @DisplayName("should throw exception when address does not exist for company")
            void updateAddressNotFound() {
                when(repository.findByIdAndOwnerId(1L, 1L))
                        .thenReturn(Optional.of(aCompany().build()));

                var request = new CompanyAddressRequest(null,
                        List.of(new AddressUpdateRequest(1L, "68 Hope Blvd", "", "Futura", "AK", "99999", Set.of())),
                        null);

                assertThatThrownBy(() -> sut.updateCompany(1L, request))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("Address 1 does not exist for company 1");

                verify(repository, never()).save(any(Company.class));

            }
        }
    }

    @Nested
    @DisplayName("removeCompany")
    class RemoveCompany {
        @BeforeEach
        void initialize() {
            when(profileService.getCurrentUserId()).thenReturn(1L);
        }

        @Test
        @DisplayName("should remove existing company")
        void happyPath() {
            when(repository.findByIdAndOwnerId(1L, 1L)).thenReturn(Optional.of(aCompany().build()));

            sut.removeCompany(1L);

            verify(repository).delete(any(Company.class));
        }

        @Test
        @DisplayName("should throw exception when company does not exist")
        void companyNotFound() {
            when(repository.findByIdAndOwnerId(1L, 1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> sut.removeCompany(1L))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage(getEntityIdForOwnerNotFoundMessage(Company.class, 1L, 1L));

            verify(repository, never()).delete(any(Company.class));
        }
    }
}
