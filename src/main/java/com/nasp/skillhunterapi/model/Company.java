package com.nasp.skillhunterapi.model;

import com.nasp.skillhunterapi.enums.CompanyType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "companies")
public class Company extends OwnedEntity {
    public Company() {
        this.companyTypes = Set.of();
        this.addresses = new ArrayList<>();
    }
    public Company(String name, String website, String industry, Set<CompanyType> companyTypes, List<Address> addresses) {
        this.name = name;
        this.website = website;
        this.industry = industry;
        this.companyTypes = companyTypes == null ? Set.of() : companyTypes;
        this.addresses = new ArrayList<>();

        if (addresses != null) {
            addresses.forEach(this::addAddress);
        }
    }

    @NotBlank
    private String name;
    private String website;
    private String industry;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<CompanyType> companyTypes;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<CompanyContactRelationship> contactRelationships;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Position> positions;

    @OneToMany(mappedBy = "employerCompany", cascade = CascadeType.ALL)
    private List<Placement> placements;

    public void addAddress(@NotNull Address address) {
        Objects.requireNonNull(address, "address must not be null");
        address.setCompany(this);
        addresses.add(address);
    }

    public Optional<Address> getAddressById(Long addressId) {
        return addresses.stream().filter(address -> address.getId().equals(addressId)).findFirst();
    }

    public void removeAddressById(Long addressId) {
        var optionalAddress = getAddressById(addressId);

        if (optionalAddress.isPresent()) {
            var address = optionalAddress.get();
            addresses.remove(address);
            address.setCompany(null);
        }
    }
}
