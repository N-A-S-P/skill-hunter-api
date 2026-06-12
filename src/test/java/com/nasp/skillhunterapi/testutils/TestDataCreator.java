package com.nasp.skillhunterapi.testutils;

import com.nasp.skillhunterapi.enums.CompanyType;
import com.nasp.skillhunterapi.model.Address;
import com.nasp.skillhunterapi.model.AppUser;
import com.nasp.skillhunterapi.model.Company;

import java.util.List;
import java.util.Set;

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
