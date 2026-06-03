package com.nasp.skillhunterapi.model;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class OwnedEntity extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    protected AppUser owner;
}
