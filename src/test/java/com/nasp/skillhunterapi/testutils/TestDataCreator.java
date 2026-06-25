package com.nasp.skillhunterapi.testutils;

import com.nasp.skillhunterapi.model.Profile;

public final class TestDataCreator {
    private TestDataCreator() {}

    public static Profile createAppUser(
            Long id,
            String userName,
            String display
    ) {
        var user = new Profile(userName, display, "", "", "", "");
        user.setId(id);
        return user;
    }
}
