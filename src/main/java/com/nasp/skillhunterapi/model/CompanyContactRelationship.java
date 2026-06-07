package com.nasp.skillhunterapi.model;

import com.nasp.skillhunterapi.enums.RelationshipType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "company_contact_relationships")
public class CompanyContactRelationship extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "contact_id", nullable = false)
    private Contact contact;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
    private Boolean isCompanyEmployee;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RelationshipType relationshipType;
    private Boolean isCurrentRelationship;

    private LocalDateTime startedOn;
    private LocalDateTime endedOn;

}
