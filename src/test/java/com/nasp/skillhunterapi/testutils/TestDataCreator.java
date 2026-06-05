package com.nasp.skillhunterapi.testutils;

import com.nasp.skillhunterapi.model.AppUser;

public final class TestDataCreator {
    private TestDataCreator() {}

    public static AppUser createAppUser(
            Long id,
            String userName,
            String display
    ) {
        var user = new AppUser(userName, display);
        user.setId(id);
        return user;
    }
}
