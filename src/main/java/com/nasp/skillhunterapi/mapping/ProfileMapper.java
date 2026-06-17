package com.nasp.skillhunterapi.mapping;

import com.nasp.skillhunterapi.dto.Profile.ProfileResponse;
import com.nasp.skillhunterapi.dto.Profile.AppUserRequest;
import com.nasp.skillhunterapi.model.Profile;
import org.springframework.stereotype.Component;

@Component
public class ProfileMapper implements BaseEntityMapper<Profile, ProfileResponse, AppUserRequest, AppUserRequest> {

    @Override
    public ProfileResponse toResponse(Profile user) {
        return new ProfileResponse(
                user.getId(),
                user.getUserName(),
                user.getDisplayName(),
                user.getGivenName(),
                user.getFamilyName(),
                user.getEmail()
        );
    }

    @Override
    public Profile toEntity(AppUserRequest request) {
        return new Profile(request.userName(), request.display(), "", "", "", "");
    }

    @Override
    public void updateEntity(Profile user, AppUserRequest request) {
        user.setUserName(request.userName());
        user.setDisplayName(request.display());
    }
}
