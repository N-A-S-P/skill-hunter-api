package com.nasp.skillhunterapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.nasp.skillhunterapi.enums.ContactMethodType;

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

    @OneToMany(mappedBy = "placedByContact")
    private List<Placement> placements;

    @ManyToMany(mappedBy = "contacts")
    private List<Interaction> interactions;

    @OneToMany(mappedBy = "contact", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContactMethod> contactMethods;

    public void addContactMethod(@NotNull ContactMethod contactMethod) {
        Objects.requireNonNull(contactMethod, "contact method must not be null");

        if (contactMethod.isPreferred() && contactMethods.stream()
                .filter(ContactMethod::isPreferred)
                .map(ContactMethod::getType)
                .anyMatch(methodType -> methodType.equals(contactMethod.getType()))) {
            throw new IllegalArgumentException("only one preferred contact method allowed per type");
        }
        contactMethods.add(contactMethod);
        contactMethod.setContact(this);
    }

    public Optional<ContactMethod> getContactMethodById(Long id) {
        return contactMethods.stream().filter(method -> method.getId().equals(id)).findFirst();
    }

    public boolean hasPreferredContactMethodOfType(ContactMethodType type, Long... excludeIds) {
        return contactMethods.stream()
                .filter(method -> !List.of(excludeIds).contains(method.getId()))
                .anyMatch(method -> method.isPreferred() && method.getType().equals(type));
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
