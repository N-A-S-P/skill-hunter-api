package com.nasp.skillhunterapi.mapping;

import com.nasp.skillhunterapi.dto.AppUser.AppUserResponse;
import com.nasp.skillhunterapi.dto.AppUser.AppUserRequest;
import com.nasp.skillhunterapi.model.AppUser;
import org.springframework.stereotype.Component;

@Component
public class AppUserMapper implements BaseEntityMapper<AppUser, AppUserResponse, AppUserRequest, AppUserRequest> {

    @Override
    public AppUserResponse toResponse(AppUser user) {
        return new AppUserResponse(
                user.getId(),
                user.getUserName(),
                user.getDisplay()
        );
    }

    @Override
    public AppUser toEntity(AppUserRequest request) {
        return new AppUser(request.userName(), request.display());
    }

    @Override
    public void updateEntity(AppUser user, AppUserRequest request) {
        user.setUserName(request.userName());
        user.setDisplay(request.display());
    }
}
