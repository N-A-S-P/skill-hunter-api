package com.nasp.skillhunterapi.model;

import com.nasp.skillhunterapi.enums.PositionType;
import com.nasp.skillhunterapi.enums.WorkLocation;
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
@Table(name = "positions")
public class Position extends OwnedEntity {
    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @OneToOne(mappedBy = "position", cascade = CascadeType.ALL)
    private PositionApplication application;

    private String title;

    private String source;

    @Lob
    private String description;

    private Integer minimumSalaryOffered;
    private Integer maximumSalaryOffered;

    @Enumerated(EnumType.STRING)
    private WorkLocation workLocation;

    @Enumerated(EnumType.STRING)
    private PositionType positionType;

    @ManyToMany(mappedBy = "positions")
    private List<Interaction> interactions;
}
