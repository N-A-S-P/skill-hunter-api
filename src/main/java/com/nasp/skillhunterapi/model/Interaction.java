package com.nasp.skillhunterapi.model;

import com.nasp.skillhunterapi.enums.InteractionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "interactions")
public class Interaction extends OwnedEntity {
    private String title;
    private LocalDateTime occurredOn;

    @Enumerated(EnumType.STRING)
    private InteractionType interactionType;

    @Lob
    private String summary;

    @ManyToMany
    @JoinTable(
        name = "interaction_contacts",
        joinColumns = @JoinColumn(name = "interaction_id"),
        inverseJoinColumns = @JoinColumn(name = "contact_id")
    )
    private List<Contact> contacts;

    @ManyToMany
    @JoinTable(
        name = "interaction_positions",
        joinColumns = @JoinColumn(name = "interaction_id"),
        inverseJoinColumns = @JoinColumn(name = "position_id")
    )
    private List<Position> positions;
}
