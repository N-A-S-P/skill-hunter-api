package com.nasp.skillhunterapi.service;

import com.nasp.skillhunterapi.dto.Profile.AppUserRequest;
import com.nasp.skillhunterapi.dto.Profile.ProfileResponse;
import com.nasp.skillhunterapi.mapping.BaseEntityMapper;
import com.nasp.skillhunterapi.mapping.ProfileMapper;
import com.nasp.skillhunterapi.model.Profile;
import com.nasp.skillhunterapi.repository.AppUserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@ExtendWith(MockitoExtension.class)
public class ProfileServiceTests {
    private final BaseEntityMapper<Profile, ProfileResponse, AppUserRequest, AppUserRequest> mapper =
            new ProfileMapper();

    @MockitoBean
    private AppUserRepository repository;

    private ProfileService sut;

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {

    }

}
