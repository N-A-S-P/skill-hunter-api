package com.nasp.skillhunterapi.model;

import com.nasp.skillhunterapi.enums.PositionApplicationStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "position_application_history")
public class PositionApplicationHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "position_application_id", nullable = false)
    private PositionApplication positionApplication;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private PositionApplicationStatus status;

    @CreationTimestamp
    private LocalDateTime changedAt;

    @Column(length = 1000)
    private String notes;
}