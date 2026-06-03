package com.nasp.skillhunterapi.model;

import com.nasp.skillhunterapi.enums.EntityType;
import jakarta.persistence.*;
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
    @Enumerated(EnumType.STRING)
    private EntityType entityType;
    private Long entityId;

    @Lob
    private String content;
}
