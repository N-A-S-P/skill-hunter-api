package com.nasp.skillhunterapi.model;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
public abstract class OwnedEntity extends BaseEntity {
    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    protected AppUser owner;
}
