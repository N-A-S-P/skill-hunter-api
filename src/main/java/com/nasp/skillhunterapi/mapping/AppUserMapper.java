package com.nasp.skillhunterapi.mapping;

import com.nasp.skillhunterapi.dto.AppUserDto;
import com.nasp.skillhunterapi.dto.AppUserRequest;
import com.nasp.skillhunterapi.model.AppUser;
import org.springframework.stereotype.Component;

@Component
public class AppUserMapper implements Mapper<AppUser, AppUserDto, AppUserRequest, AppUserRequest> {

    @Override
    public AppUserDto toDto(AppUser user) {
        return new AppUserDto(
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
