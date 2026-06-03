package com.nasp.skillhunterapi.model;

import com.nasp.skillhunterapi.enums.PositionApplicationStatus;
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
@Table(name = "position_application_history")
public class PositionApplicationHistory extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "position_application_id", nullable = false)
    private PositionApplication positionApplication;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PositionApplicationStatus status;

    private LocalDateTime changedAt;

    @Column(length = 1000)
    private String notes;
}