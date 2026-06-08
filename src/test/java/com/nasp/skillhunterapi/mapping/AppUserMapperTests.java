package com.nasp.skillhunterapi.mapping;

import com.nasp.skillhunterapi.dto.AppUser.AppUserResponse;
import com.nasp.skillhunterapi.dto.AppUser.AppUserRequest;
import com.nasp.skillhunterapi.model.AppUser;
import org.junit.jupiter.api.Test;

import static com.nasp.skillhunterapi.testutils.TestDataCreator.createAppUser;
import static org.assertj.core.api.Assertions.assertThat;

public class AppUserMapperTests {
    @Test
    void toResponse_ShouldMapAppUserToAppUserResponse() {
        var sut = new AppUserMapper();
        var entity = createAppUser(1L, "testuser", "Test User");

        AppUserResponse result = sut.toResponse(entity);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.userName()).isEqualTo("testuser");
        assertThat(result.display()).isEqualTo("Test User");
    }

    @Test
    void toEntity_ShouldMapAppUserRequestToAppUser() {
        var sut = new AppUserMapper();
        var request = new AppUserRequest("testuser", "Test User");

        AppUser result = sut.toEntity(request);

        assertThat(result.getUserName()).isEqualTo("testuser");
        assertThat(result.getDisplay()).isEqualTo("Test User");
    }

    @Test
    void updateEntity_ShouldUpdateAppUserFromAppUserRequest() {
        var sut = new AppUserMapper();
        var entity = createAppUser(1L, "testuser", "Test User");
        var request = new AppUserRequest("test_user", "Test User, Jr.");

        sut.updateEntity(entity, request);

        assertThat(entity.getUserName()).isNotEqualTo("testuser");
        assertThat(entity.getUserName()).isEqualTo("test_user");
        assertThat(entity.getDisplay()).isNotEqualTo("Test User");
        assertThat(entity.getDisplay()).isEqualTo("Test User, Jr.");
    }
}
