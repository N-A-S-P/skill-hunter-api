package com.nasp.skillhunterapi.model;

import com.nasp.skillhunterapi.enums.ContactMethodContext;
import com.nasp.skillhunterapi.enums.ContactMethodType;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "contact_methods")
public class ContactMethod extends BaseEntity {
    public ContactMethod(ContactMethodType type, ContactMethodContext context, String value, boolean isPreferred) {
        this.type = type;
        this.context = context;
        this.value = value;
        this.isPreferred = isPreferred;
    }

    @ManyToOne(optional = false)
    private Contact contact;

    @Enumerated(EnumType.STRING)
    private ContactMethodType type;

    @Enumerated(EnumType.STRING)
    private ContactMethodContext context;

    private String value;

    private boolean isPreferred;
}
