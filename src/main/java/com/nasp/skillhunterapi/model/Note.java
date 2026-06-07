package com.nasp.skillhunterapi.model;

import com.nasp.skillhunterapi.enums.EntityType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notes")
public class Note extends OwnedEntity {
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EntityType entityType;

    @NotNull
    @Column(nullable = false)
    private Long entityId;

    private String subject;

    @NotNull
    @Lob
    @Column(nullable = false)
    private String content;
}
