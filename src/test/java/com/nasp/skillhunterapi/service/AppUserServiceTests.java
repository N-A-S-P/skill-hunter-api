package com.nasp.skillhunterapi.service;

import com.nasp.skillhunterapi.dto.Profile.ProfileResponse;
import com.nasp.skillhunterapi.dto.Profile.AppUserRequest;
import com.nasp.skillhunterapi.mapping.ProfileMapper;
import com.nasp.skillhunterapi.model.Profile;
import com.nasp.skillhunterapi.repository.AppUserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.nasp.skillhunterapi.testutils.TestDataCreator.createAppUser;
import static com.nasp.skillhunterapi.util.ExceptionMessages.getEntityIdNotFoundMessage;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class AppUserServiceTests {
    private AppUserRepository repository;
    private AppUserService sut;

    /* constants */
    private final String duplicatedUsernameMessage = "User with username \"%s\" already exists";

    @BeforeEach
    void initialize() {
        var mapper = new ProfileMapper();
        repository = mock(AppUserRepository.class);
        sut = new AppUserService(repository, mapper);
    }

    @Nested
    @DisplayName("getAllAppUsers")
    class GetAllAppUsers {
        @Test
        @DisplayName("should return empty list when no AppUsers exist")
        void shouldReturnEmptyList() {
            when(repository.findAll()).thenReturn(new ArrayList<>());

            var result = sut.getAllAppUsers();

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("should return list of mapped AppUser DTOs")
        void shouldReturnMappedAppUserDtos() {
            when(repository.findAll())
                    .thenReturn(List.of(
                            createAppUser(1L, "testuser", "Amanda"),
                            createAppUser(2L, "test_user", "Bianca")));

            var result = sut.getAllAppUsers();

            assertThat(result).hasSize(2);

            assertThat(result)
                    .extracting(ProfileResponse::id)
                    .containsExactly(1L, 2L);

            assertThat(result)
                    .extracting(ProfileResponse::userName)
                    .containsExactly("testuser", "test_user");

            assertThat(result)
                    .extracting(ProfileResponse::display)
                    .containsExactly("Amanda", "Bianca");
        }
    }

    @Nested
    @DisplayName("getAppUserById")
    class GetProfileById {
        @Test
        @DisplayName("should return mapped AppUser DTO")
        void happyPath() {
            when(repository.findById(anyLong()))
                    .thenReturn(Optional.of(
                            createAppUser(1L, "testuser", "Test User")));

            var result = sut.getAppUserById(1L);

            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(1L);
            assertThat(result.userName()).isEqualTo("testuser");
            assertThat(result.display()).isEqualTo("Test User");

        }

        @Test
        @DisplayName("should throw an error when user does not exist")
        void userNotExist() {
            when(repository.findById(anyLong())).thenReturn(Optional.empty());

            assertThatThrownBy(() -> sut.getAppUserById(1L))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage(getEntityIdNotFoundMessage(Profile.class, 1L));
        }
    }

    @Nested
    @DisplayName("getAppUserByUserName")
    class GetProfileName {
        @Test
        @DisplayName("should returned mapped AppUser DTO")
        void happyPath() {
            when(repository.findByUserNameIgnoreCase(anyString()))
                    .thenReturn(Optional.of(
                            createAppUser(1L, "testuser", "Test User")
                    ));

            var result = sut.getAppUserByUserName("testuser");

            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(1L);
            assertThat(result.userName()).isEqualTo("testuser");
            assertThat(result.display()).isEqualTo("Test User");
        }

        @Test
        @DisplayName("should throw an error when user does not exist")
        void userNotExist() {
            when(repository.findByUserNameIgnoreCase(anyString())).thenReturn(Optional.empty());

            assertThatThrownBy(() -> sut.getAppUserByUserName("testuser"))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage("Could not find AppUser with username \"testuser\"");
        }
    }

    @Nested
    @DisplayName("createAppUser")
    class CreateProfile {
        @Test
        @DisplayName("should create a new user and return the mapped DTO")
        void happyPath() {
            when(repository.existsByUserNameIgnoreCase(anyString()))
                    .thenReturn(false);
            when(repository.save(any(Profile.class)))
                    .thenReturn(createAppUser(1L, "testuser", "Test User"));

            var result = sut.createAppUser(
                    new AppUserRequest("testuser", "Test User")
            );

            verify(repository).save(any(Profile.class));

            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(1L);
            assertThat(result.userName()).isEqualTo("testuser");
            assertThat(result.display()).isEqualTo("Test User");
        }

        @Test
        @DisplayName("should throw when trying to create a user with existing username")
        void dupeUserName() {
            when(repository.existsByUserNameIgnoreCase(anyString())).thenReturn(true);

            assertThatThrownBy(() -> sut.createAppUser(new AppUserRequest("testuser", "Test User")))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(duplicatedUsernameMessage.formatted("testuser"));

            verify(repository, never()).save(any(Profile.class));
        }
    }

    @Nested
    @DisplayName("updateAppUser")
    class UpdateProfile {
        @Test
        @DisplayName("should update an existing user and return the mapped DTO")
        void happyPath() {
            var existingUser = createAppUser(1L, "testuser", "Test User");
            when(repository.findById(anyLong()))
                    .thenReturn(Optional.of(existingUser));
            when(repository.findByUserNameIgnoreCase(anyString()))
                    .thenReturn(Optional.empty());
            when(repository.save(any(Profile.class)))
                    .thenAnswer(invocation ->
                            invocation.getArgument(0));

            var result = sut.updateAppUser(1L, new AppUserRequest("test_user", "Test User, Jr."));

            verify(repository).save(any(Profile.class));

            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(1L);
            assertThat(result.userName()).isEqualTo("test_user");
            assertThat(result.display()).isEqualTo("Test User, Jr.");
        }

        @Test
        @DisplayName("should throw when user does not exist")
        void userNotExist() {
            when(repository.findByUserNameIgnoreCase(anyString()))
                    .thenReturn(Optional.empty());
            when(repository.findById(anyLong()))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> sut.updateAppUser(1L, new AppUserRequest("testuser", "Test User")))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage(getEntityIdNotFoundMessage(Profile.class, 1L));

            verify(repository, never()).save(any(Profile.class));
        }

        @Test
        @DisplayName("should not throw when not updating user's username")
        void userNameUnchanged() {
            var entity = createAppUser(1L, "testuser", "Beyonce");
            when(repository.findByUserNameIgnoreCase(anyString()))
                    .thenReturn(Optional.of(entity));
            when(repository.findById(anyLong()))
                    .thenReturn(Optional.of(entity));

            var result = sut.updateAppUser(1L, new AppUserRequest("testuser", "Adele"));

            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(1L);
            assertThat(result.userName()).isEqualTo("testuser");
            assertThat(result.display()).isEqualTo("Adele");

            verify(repository).findById(anyLong());
            verify(repository).save(any(Profile.class));
        }

        @Test
        @DisplayName("should throw when trying to set username to other user's username")
        void dupeUserName() {
            when(repository.findByUserNameIgnoreCase(anyString()))
                    .thenReturn(
                            Optional.of(createAppUser(2L, "testuser", "Beyonce")));
            var request = new AppUserRequest("testuser", "Adele");

            assertThatThrownBy(() -> sut.updateAppUser(1L, request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(duplicatedUsernameMessage.formatted(request.userName()));

            verify(repository, never()).findById(anyLong());
            verify(repository, never()).save(any(Profile.class));
        }
    }

    @Nested
    @DisplayName("deleteAppUser")
    class DeleteProfile {
        @Test
        @DisplayName("should delete existing user")
        void happyPath() {
            when(repository.existsById(anyLong())).thenReturn(true);

            sut.deleteAppUser(1L);

            verify(repository).deleteById(anyLong());
        }

        @Test
        @DisplayName("should throw an error if user does not exist")
        void userNotExist() {
            when(repository.existsById(anyLong())).thenReturn(false);

            assertThatThrownBy(() -> sut.deleteAppUser(1L))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage(getEntityIdNotFoundMessage(Profile.class, 1L));

            verify(repository, never()).deleteById(anyLong());
        }
    }
}
