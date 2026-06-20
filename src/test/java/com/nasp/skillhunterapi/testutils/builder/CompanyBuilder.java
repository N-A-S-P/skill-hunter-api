package com.nasp.skillhunterapi.testutils.builder;

import com.nasp.skillhunterapi.enums.CompanyType;
import com.nasp.skillhunterapi.model.Address;
import com.nasp.skillhunterapi.model.Profile;
import com.nasp.skillhunterapi.model.Company;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.nasp.skillhunterapi.testutils.builder.ProfileBuilder.aProfile;

public final class CompanyBuilder {
    private Long id = 1L;
    private String name = "Bumblebee Debuggers";
    private String website = "http://www.de-bumbler.org";
    private String industry = "Technology";
    private Set<CompanyType> companyTypes = new HashSet<>(Set.of(CompanyType.STAFFING_FIRM));
    private List<Address> addresses = new ArrayList<>();

    private Profile owner = aProfile().build();

    private CompanyBuilder() {
    }

    public static CompanyBuilder aCompany() {
        return new CompanyBuilder();
    }

    public CompanyBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public CompanyBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public CompanyBuilder withWebsite(String website) {
        this.website = website;
        return this;
    }

    public CompanyBuilder withIndustry(String industry) {
        this.industry = industry;
        return this;
    }

    public CompanyBuilder withCompanyTypes(CompanyType... companyTypes) {
        this.companyTypes = new HashSet<>(List.of(companyTypes));
        return this;
    }

    public CompanyBuilder addCompanyType(CompanyType companyType) {
        this.companyTypes.add(companyType);
        return this;
    }

    public CompanyBuilder withAddresses(Address... addresses) {
        this.addresses = new ArrayList<>(List.of(addresses));
        return this;
    }

    public CompanyBuilder addAddress(Address address) {
        this.addresses.add(address);
        return this;
    }

    public CompanyBuilder withOwner(Profile owner) {
        this.owner = owner;
        return this;
    }

    public Company build() {
        var company = new Company(name, website, industry, companyTypes, addresses);
        company.setId(id);
        company.setOwner(owner);

        return company;
    }
}
