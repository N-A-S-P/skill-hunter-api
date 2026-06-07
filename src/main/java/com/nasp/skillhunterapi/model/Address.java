package com.nasp.skillhunterapi.model;

import com.nasp.skillhunterapi.enums.AddressType;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "addresses")
public class Address extends BaseEntity {
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
