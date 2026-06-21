package com.nasp.skillhunterapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@Entity
@Table(name = "contacts")
public class Contact extends OwnedEntity {
    public Contact() {
        initializeCollections();
    }

    public Contact(
            String firstName,
            String lastName,
            String title) {
        initializeCollections();
        this.firstName = firstName;
        this.lastName = lastName;
        this.title = title;
    }

    public Contact(
            String firstName,
            String lastName,
            String title,
            List<ContactMethod> contactMethods) {
        initializeCollections();
        this.firstName = firstName;
        this.lastName = lastName;
        this.title = title;

        if (contactMethods != null) {
            contactMethods.forEach(this::addContactMethod);
        }
    }

    @NotNull
    @Column(nullable = false)
    private String firstName;
    @NotNull
    @Column(nullable = false)
    private String lastName;

    private String title;

    @OneToMany(mappedBy = "contact", cascade = CascadeType.ALL)
    private List<CompanyContactRelationship> companyRelationships;

    @OneToMany(mappedBy = "placedByContact", cascade = CascadeType.ALL)
    private List<Placement> placements;

    @ManyToMany(mappedBy = "contacts")
    private List<Interaction> interactions;

    @OneToMany(mappedBy = "contact")
    private List<ContactMethod> contactMethods;

    public void addContactMethod(ContactMethod contactMethod) {
        contactMethods.add(contactMethod);
        contactMethod.setContact(this);
    }

    public Optional<ContactMethod> getContactMethodById(Long id) {
        return contactMethods.stream().filter(method -> method.getId().equals(id)).findFirst();
    }

    public void removeContactMethod(Long id) {
        getContactMethodById(id)
                .ifPresent(method -> {
                    contactMethods.remove(method);
                    method.setContact(null);
                });
    }

    public void removeRelationships() {
        contactMethods.forEach(method -> method.setContact(null));
        contactMethods.clear();

        companyRelationships.clear();

        placements.clear();

        interactions.clear();
    }

    private void initializeCollections() {
        contactMethods = new ArrayList<>();
        companyRelationships = new ArrayList<>();
        placements = new ArrayList<>();
        interactions = new ArrayList<>();
    }
}
