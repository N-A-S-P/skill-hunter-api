package com.nasp.skillhunterapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "placements")
public class Placement extends BaseEntity {
    @OneToOne
    @JoinColumn(name = "position_id", nullable = false)
    private Position position;

    @ManyToOne
    @JoinColumn(name = "placed_by_id")
    private Contact placedByContact;

    @ManyToOne
    @JoinColumn(name = "employer_company_id")
    // If not through a staffing agency, leave null.
    private Company employerCompany;

    private LocalDate startedOn;
    private LocalDate endedOn;

    @ManyToMany
    @JoinTable(
            name = "placement_skills",
            joinColumns = @JoinColumn(name = "placement_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private List<Skill> skills;
}
