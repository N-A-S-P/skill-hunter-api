package com.nasp.skillhunterapi.model;

import com.nasp.skillhunterapi.enums.CompanyType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "companies")
public class Company extends OwnedEntity {
    private String name;
    private String website;
    private String industry;

    @OneToMany(mappedBy="company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<CompanyContactRelationship> contactRelationships;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<Position> positions;

    @OneToMany(mappedBy = "employerCompany", cascade = CascadeType.ALL)
    private List<Placement> placements;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<CompanyType> companyTypes;
}
