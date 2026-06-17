package com.nasp.skillhunterapi.mapping;

import com.nasp.skillhunterapi.dto.Profile.ProfileResponse;
import com.nasp.skillhunterapi.dto.Profile.AppUserRequest;
import com.nasp.skillhunterapi.model.Profile;
import org.junit.jupiter.api.Test;

import static com.nasp.skillhunterapi.testutils.TestDataCreator.createAppUser;
import static com.nasp.skillhunterapi.testutils.builder.ProfileBuilder.aProfile;
import static org.assertj.core.api.Assertions.assertThat;

public class ProfileMapperTests {
    @Test
    void toResponse_ShouldMapAppUserToAppUserResponse() {
        var sut = new ProfileMapper();
        var entity = aProfile().build();

        ProfileResponse result = sut.toResponse(entity);

        assertThat(result.id()).isEqualTo(entity.getId());
        assertThat(result.userName()).isEqualTo(entity.getUserName());
        assertThat(result.display()).isEqualTo(entity.getDisplayName());
        assertThat(result.givenName()).isEqualTo(entity.getGivenName());
        assertThat(result.familyName()).isEqualTo(entity.getFamilyName());
        assertThat(result.email()).isEqualTo(entity.getEmail());
    }

    @Test
    void toEntity_ShouldMapAppUserRequestToAppUser() {
        var sut = new ProfileMapper();
        var request = new AppUserRequest("testuser", "Test User");

        Profile result = sut.toEntity(request);

        assertThat(result.getUserName()).isEqualTo("testuser");
        assertThat(result.getDisplayName()).isEqualTo("Test User");
    }

    @Test
    void updateEntity_ShouldUpdateAppUserFromAppUserRequest() {
        var sut = new ProfileMapper();
        var entity = aProfile().build();
        var request = new AppUserRequest("test_user", "Test User, Jr.");

        sut.updateEntity(entity, request);

        assertThat(entity.getUserName()).isEqualTo("test_user");
        assertThat(entity.getDisplayName()).isEqualTo("Test User, Jr.");
    }
}
