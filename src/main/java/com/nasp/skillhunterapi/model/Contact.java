package com.nasp.skillhunterapi.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contacts")
public class Contact extends OwnedEntity {
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String phoneNumber;
    private String linkedInProfile;
    private String title;

    @OneToMany(mappedBy = "contact", cascade = CascadeType.ALL)
    private List<CompanyContactRelationship> companyRelationships;

    @OneToMany(mappedBy = "placedByContact", cascade = CascadeType.ALL)
    private List<Placement> placements;

    @ManyToMany(mappedBy = "contacts")
    private List<Interaction> interactions;
}
