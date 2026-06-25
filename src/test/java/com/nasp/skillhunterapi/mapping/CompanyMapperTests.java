package com.nasp.skillhunterapi.mapping;

import com.nasp.skillhunterapi.dto.Company.AddressCreateRequest;
import com.nasp.skillhunterapi.dto.Company.CompanyCreateRequest;
import com.nasp.skillhunterapi.dto.Company.CompanyUpdateRequest;
import com.nasp.skillhunterapi.enums.CompanyType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static com.nasp.skillhunterapi.testutils.LookupResponseAssertions.matchesLookup;
import static com.nasp.skillhunterapi.testutils.builder.AddressBuilder.anAddress;
import static com.nasp.skillhunterapi.testutils.builder.CompanyBuilder.aCompany;
import static com.nasp.skillhunterapi.testutils.builder.ProfileBuilder.aProfile;
import static org.assertj.core.api.Assertions.assertThat;

public class CompanyMapperTests {
    private final LookupMapper lookupMapper = new LookupMapper();
    private final AddressMapper addressMapper = new AddressMapper(lookupMapper);

    @Test
    @DisplayName("should map Company to CompanyListItemResponse")
    void toListItemResponse() {
        var sut = new CompanyMapper(addressMapper, lookupMapper);
        var company = aCompany().build();

        var result = sut.toListItemResponse(company);

        assertThat(result.id()).isEqualTo(company.getId());
        assertThat(result.name()).isEqualTo(company.getName());
        assertThat(result.website()).isEqualTo(company.getWebsite());
        assertThat(result.industry()).isEqualTo(company.getIndustry());
        assertThat(result.companyTypes()).singleElement()
                .satisfies(matchesLookup(CompanyType.STAFFING_FIRM));
    }

    @Test
    @DisplayName("should map Company to CompanyDetailResponse")
    void toDetailResponse() {
        var sut = new CompanyMapper(addressMapper, lookupMapper);
        var company = aCompany()
                .withAddresses(anAddress().build())
                .build();

        var result = sut.toDetailResponse(company);

        assertThat(result.id()).isEqualTo(company.getId());
        assertThat(result.name()).isEqualTo(company.getName());
        assertThat(result.website()).isEqualTo(company.getWebsite());
        assertThat(result.companyTypes()).singleElement()
                .satisfies(matchesLookup(CompanyType.STAFFING_FIRM));
        assertThat(result.addresses()).singleElement().satisfies(address -> {

        });
    }

    @Test
    @DisplayName("should map CompanyCreateRequest to Company")
    void toEntity() {
        var sut = new CompanyMapper(addressMapper, lookupMapper);
        var request = new CompanyCreateRequest("Bumblebee's Debuggers", "www.de-bumbler.org",
                "Technology", Set.of(CompanyType.RECRUITING_AGENCY), List.of(
                        new AddressCreateRequest("903 Tnomleb Weiv", "", "Ehtnisba", "WY", "35768", Set.of())));
        var owner = aProfile().build();
        var result = sut.toEntity(request, owner);

        assertThat(result.getName()).isEqualTo(request.name());
        assertThat(result.getWebsite()).isEqualTo(request.website());
        assertThat(result.getIndustry()).isEqualTo(request.industry());
        assertThat(result.getCompanyTypes()).containsExactly(CompanyType.RECRUITING_AGENCY);
        assertThat(result.getAddresses()).hasSize(1);
        assertThat(result.getOwner()).isEqualTo(owner);

    }

    @Test
    @DisplayName("should update Company from CompanyUpdateRequest")
    void updateEntity() {
        var sut = new CompanyMapper(addressMapper, lookupMapper);
        var company = aCompany().build();
        var request = new CompanyUpdateRequest("Witwicky Solutions", "https://www.witwicky.com", "Healthcare", Set.of(CompanyType.HIRER));

        sut.updateEntity(company, request);

        assertThat(company.getName()).isEqualTo(request.name());
        assertThat(company.getWebsite()).isEqualTo(request.website());
        assertThat(company.getIndustry()).isEqualTo(request.industry());
        assertThat(company.getCompanyTypes()).containsExactly(CompanyType.HIRER);
    }
}
