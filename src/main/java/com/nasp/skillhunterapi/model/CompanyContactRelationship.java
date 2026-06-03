package com.nasp.skillhunterapi.model;

import com.nasp.skillhunterapi.enums.RelationshipType;
import jakarta.persistence.*;

@Entity
@Table(name = "company_contact_relationships")
public class CompanyContactRelationship extends BaseEntity{
    @ManyToOne
    @JoinColumn(name = "contact_id", nullable = false)
    private Contact contact;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RelationshipType relationshipType;
}
