package com.nasp.skillhunterapi.model;

import com.nasp.skillhunterapi.enums.PositionApplicationStatus;
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
@Table(name = "applications")
public class PositionApplication extends BaseEntity {
    @OneToOne
    @JoinColumn(name = "position_id", nullable = false)
    private Position position;

    private LocalDate appliedOn;
    private Boolean isActive; // accepting applications

    @Enumerated(EnumType.STRING)
    private PositionApplicationStatus status;

    @OneToMany(mappedBy = "positionApplication")
    private List<PositionApplicationHistory> updates;
}
