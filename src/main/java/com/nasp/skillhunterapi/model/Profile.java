package com.nasp.skillhunterapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "app_user")
public class Profile extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String userName;
    private String displayName;
    private String givenName;
    private String familyName;
    private String email;

    @Column(nullable = false, unique = true)
    private String externalSubject;
}
