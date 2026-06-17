package com.nasp.skillhunterapi.testutils.builder;

import com.nasp.skillhunterapi.model.Profile;

import java.util.UUID;

public class ProfileBuilder {
    private Long id = 1L;
    private String userName = "i_own_u";
    private String displayName = "Hades";
    private String keycloakSubject = "12345678-1234-1234-1234-123456789012";
    private String givenName = "Hades";
    private String familyName = "Lord of the Underworld";
    private String email = "hades@godsofolympus.he.org";

    public static ProfileBuilder aProfile() {
        return new ProfileBuilder();
    }

    public ProfileBuilder withId(Long value) {
        id = value;
        return this;
    }

    public ProfileBuilder withUserName(String value) {
        userName = value;
        return this;
    }

    public ProfileBuilder withDisplayName(String value) {
        displayName = value;
        return this;
    }

    public ProfileBuilder withGivenName(String value) {
        givenName = value;
        return this;
    }

    public ProfileBuilder withFamilyName(String value) {
        familyName = value;
        return this;
    }

    public ProfileBuilder withEmail(String value) {
        email = value;
        return this;
    }

    public ProfileBuilder withKeycloakSubject(String value) {
        keycloakSubject = value;
        return this;
    }

    public ProfileBuilder withRandomKeycloakSubject() {
        keycloakSubject = UUID.randomUUID().toString();
        return this;
    }

    public Profile build() {
        var profile = new Profile(userName, displayName, givenName, familyName, email, keycloakSubject);
        profile.setId(id);

        return profile;
    }
}
