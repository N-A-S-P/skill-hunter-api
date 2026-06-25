package com.nasp.skillhunterapi.testutils;

import com.nasp.skillhunterapi.model.Profile;
import com.nasp.skillhunterapi.provider.CurrentUserProvider;
import static com.nasp.skillhunterapi.testutils.builder.ProfileBuilder.aProfile;

public class TestCurrentUserProvider implements CurrentUserProvider {
    @Override
    public Long getCurrentUserId() {
        return 1L;
    }

    @Override
    public Profile getCurrentUser() {
        return aProfile().build();
    }
}
