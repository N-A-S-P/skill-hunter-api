package com.nasp.skillhunterapi.service;

import com.nasp.skillhunterapi.mapping.ProfileMapper;
import com.nasp.skillhunterapi.repository.AppUserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Optional;

import static com.nasp.skillhunterapi.testutils.builder.ProfileBuilder.aProfile;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProfileServiceTests {
    @Mock
    private AppUserRepository repository;

    private ProfileService sut;

    private final String SUBJECT = "12345678-1234-1234-123456789012";
    private final String NO_ASSOCIATED_PROFILE = "Could not find Profile associated with log in";

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
        sut = new ProfileService(repository, new ProfileMapper());
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Nested
    @DisplayName("getCurrentUserId")
    class GetCurrentUserId {
        @Test
        @DisplayName("should return profile id associated with external subject")
        void happyPath() {
            initializeJwt();
            when(repository.findByExternalSubject(SUBJECT)).thenReturn(Optional.of(aProfile().build()));

            var result = sut.getCurrentUserId();

            assertThat(result).isEqualTo(1L);
        }

        @Test
        @DisplayName("should throw AccessDeniedException when no auth")
        void noAuth() {
            assertThatThrownBy(() -> sut.getCurrentUserId())
                    .isInstanceOf(AccessDeniedException.class)
                    .hasMessage("No authenticated user");
        }

        @Test
        @DisplayName("should throw AccessDeniedException when principal is not jwt")
        void principalNotJwt() {
            var context = SecurityContextHolder.createEmptyContext();
            var auth = new TestingAuthenticationToken("not-a-jwt", null);
            context.setAuthentication(auth);
            SecurityContextHolder.setContext(context);
            assertThatThrownBy(() -> sut.getCurrentUserId())
                    .isInstanceOf(AccessDeniedException.class)
                    .hasMessage("No authenticated user");
        }

        @Test
        @DisplayName("should throw EntityNotFoundException when no profile id associated with external subject")
        void noProfileForSubject() {
            initializeJwt();
            when(repository.findByExternalSubject(SUBJECT)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> sut.getCurrentUserId())
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage(NO_ASSOCIATED_PROFILE);
        }
    }

    @Nested
    @DisplayName("getCurrentUser")
    class GetCurrentUser {
        @Test
        @DisplayName("should return Profile associated with external subject")
        void happyPath() {
            initializeJwt();
            var profile = aProfile().build();
            when(repository.findByExternalSubject(SUBJECT))
                    .thenReturn(Optional.of(profile));

            var result = sut.getCurrentUser();

            assertThat(result).isEqualTo(profile);
        }

        @Test
        @DisplayName("should throw AccessDeniedException when no auth")
        void unauthenticated() {
            assertThatThrownBy(() -> sut.getCurrentUser())
                    .isInstanceOf(AccessDeniedException.class);
        }

        @Test
        @DisplayName("should throw EntityNotFoundException when no profile associated with external subject")
        void noProfileForSubject() {
            initializeJwt();
            when(repository.findByExternalSubject(SUBJECT)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> sut.getCurrentUser())
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage(NO_ASSOCIATED_PROFILE);
        }
    }

    @Nested
    @DisplayName("getProfile")
    class GetProfile {
        @Test
        @DisplayName("should return mapped Profile associated with external subject")
        void happyPath() {
            initializeJwt();
            var profile = aProfile().build();
            when(repository.findByExternalSubject(SUBJECT)).thenReturn(Optional.of(profile));

            var result = sut.getProfile();

            assertThat(result.id()).isEqualTo(profile.getId());
            assertThat(result.userName()).isEqualTo(profile.getUserName());
            assertThat(result.display()).isEqualTo(profile.getDisplayName());
            assertThat(result.givenName()).isEqualTo(profile.getGivenName());
            assertThat(result.familyName()).isEqualTo(profile.getFamilyName());
            assertThat(result.email()).isEqualTo(profile.getEmail());
        }

        @Test
        @DisplayName("should throw AccessDeniedException when no auth")
        void unauthenticated() {
            assertThatThrownBy(() -> sut.getProfile())
                    .isInstanceOf(AccessDeniedException.class);
        }

        @Test
        @DisplayName("should throw EntityNotFoundException when no profile associated with external subject")
        void noProfileForSubject() {
            initializeJwt();
            when(repository.findByExternalSubject(SUBJECT)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> sut.getProfile())
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage(NO_ASSOCIATED_PROFILE);
        }
    }

    void initializeJwt() {
        var jwt = Jwt.withTokenValue("test-token")
                .header("alg", "none")
                .subject(SUBJECT)
                .build();

        var auth = new JwtAuthenticationToken(jwt);
        var context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);
    }
}
