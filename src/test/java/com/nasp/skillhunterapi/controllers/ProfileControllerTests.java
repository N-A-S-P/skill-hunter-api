package com.nasp.skillhunterapi.controllers;

import com.nasp.skillhunterapi.config.SecurityConfig;
import com.nasp.skillhunterapi.dto.Profile.ProfileResponse;
import com.nasp.skillhunterapi.service.ProfileService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.oauth2.server.resource.autoconfigure.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

@WebMvcTest(
        controllers = ProfileController.class, // sut type
        excludeAutoConfiguration = OAuth2ResourceServerAutoConfiguration.class
        // Use SecurityConfig instead of OAuth2 auto-configuration.
        // Controller tests provide a mocked JwtDecoder.
        // Removing any of them will result in PAIN
        // Ask Spring why. I chose life.
)
@Import(SecurityConfig.class)
public class ProfileControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProfileService profileService;

    @MockitoBean
    private JwtDecoder jwtDecoder; // DO NOT REMOVE OR SPRING WILL SCREAM

    @Nested
    @DisplayName("GET /api/me")
    class GetProfile {
        @Test
        @DisplayName("should return existing profile")
        void happyPath() throws Exception {
            var profile = new ProfileResponse(
                    1L,
                    "i_own_u",
                    "Hades",
                    "Hades",
                    "Lord of the Underworld",
                    "hades@godsofolympus.he.org");
            when(profileService.getProfile()).thenReturn(profile);

        mockMvc.perform(get("/api/me")
                        .with(jwt()))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(1),
                        jsonPath("$.userName").value(profile.userName()),
                        jsonPath("$.display").value(profile.display()),
                        jsonPath("$.givenName").value(profile.givenName()),
                        jsonPath("$.familyName").value(profile.familyName()),
                        jsonPath("$.email").value(profile.email())
                );
        }

        @Test
        @DisplayName("should return 401 when no user logged in")
        void unauthorized() throws Exception {
            mockMvc.perform(get("/api/me"))
                    .andExpectAll(
                            status().isUnauthorized()
                    );
        }

        @Test
        @DisplayName("should return 404 when no profile is associated with logged in user")
        void notFound() throws Exception {
            when(profileService.getProfile())
                    .thenThrow(new EntityNotFoundException());

            mockMvc.perform(get("/api/me").with(jwt()))
                    .andExpect(status().isNotFound());
        }
    }
}
