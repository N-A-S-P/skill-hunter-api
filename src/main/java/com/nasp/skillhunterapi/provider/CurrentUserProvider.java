package com.nasp.skillhunterapi.provider;

import com.nasp.skillhunterapi.model.Profile;

public interface CurrentUserProvider {
    Long getCurrentUserId();
    Profile getCurrentUser();
}
