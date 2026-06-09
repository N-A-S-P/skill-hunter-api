package com.nasp.skillhunterapi.model;

import com.nasp.skillhunterapi.enums.AddressType;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "addresses")
public class Address extends BaseEntity {
    public Address() {
        this.addressTypes = new HashSet<>();
    }

    public Address(String line1, String line2, String city, String state, String zipCode, Set<AddressType> addressTypes) {
        this.line1 = line1;
        this.line2 = line2;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.addressTypes = addressTypes == null ? new HashSet<>() : addressTypes;
    }
    private String line1;

    @Nullable
    private String line2;

    private String city;
    private String state;
    private String zipCode;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<AddressType> addressTypes;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
}
