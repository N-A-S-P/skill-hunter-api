package com.nasp.skillhunterapi.controllers;

import com.nasp.skillhunterapi.dto.AppUser.AppUserResponse;
import com.nasp.skillhunterapi.dto.AppUser.AppUserRequest;
import com.nasp.skillhunterapi.service.AppUserService;
import static com.nasp.skillhunterapi.testutils.JsonSerializer.stringify;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AppUserController.class)
class AppUserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AppUserService appUserService;

    private final AppUserRequest appUserRequest = new AppUserRequest("testuser", "Test User");

    @Test
    @DisplayName("GET /api/users should return all users")
    void getAllAppUsers() throws Exception {
        when(appUserService.getAllAppUsers())
                .thenReturn(List.of(
                        new AppUserResponse(1L, "testuser", "Test User"),
                        new AppUserResponse(2L, "anotheruser", "Another User")
                ));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].userName").value("testuser"))
                .andExpect(jsonPath("$[0].display").value("Test User"));
    }

    @Nested
    @DisplayName("GET /api/users/{id}")
    class GetAppUserById {
        @Test
        @DisplayName("should return user")
        void happyPath() throws Exception {
            when(appUserService.getAppUserById(1L))
                    .thenReturn(new AppUserResponse(1L, "testuser", "Test User"));

            mockMvc.perform(get("/api/users/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.userName").value("testuser"))
                    .andExpect(jsonPath("$.display").value("Test User"));
        }

        @Test
        @DisplayName("should return 404 when missing")
        void notFound() throws Exception {
            when(appUserService.getAppUserById(999L))
                    .thenThrow(new EntityNotFoundException("Could not find AppUser with id 999"));

            mockMvc.perform(get("/api/users/999"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value(404))
                    .andExpect(jsonPath("$.error").value("Not Found"))
                    .andExpect(jsonPath("$.message").value("Could not find AppUser with id 999"))
                    .andExpect(jsonPath("$.path").value("/api/users/999"));
        }
    }

    @Nested
    @DisplayName("GET /api/users/username/{userName}")
    class GetAppUserByUserName {
        @Test
        @DisplayName("should return user")
        void happyPath() throws Exception {
            when(appUserService.getAppUserByUserName("testuser"))
                    .thenReturn(new AppUserResponse(1L, "testuser", "Test User"));

            mockMvc.perform(get("/api/users/username/testuser"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.userName").value("testuser"))
                    .andExpect(jsonPath("$.display").value("Test User"));
        }

        @Test
        @DisplayName("should return 404 when missing")
        void notFound() throws Exception {
            when(appUserService.getAppUserByUserName("testuser"))
                    .thenThrow(new EntityNotFoundException("Could not find AppUser with username \"testuser\""));

            mockMvc.perform(get("/api/users/username/testuser"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value(404))
                    .andExpect(jsonPath("$.message").value("Could not find AppUser with username \"testuser\""))
                    .andExpect(jsonPath("$.path").value("/api/users/username/testuser"));
        }
    }

    @Nested
    @DisplayName("POST /api/users")
    class CreateAppUser {
        @Test
        @DisplayName("should create user")
        void happyPath() throws Exception {
            when(appUserService.createAppUser(any(AppUserRequest.class)))
                    .thenReturn(new AppUserResponse(1L, "testuser", "Test User"));

            mockMvc.perform(post("/api/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(stringify(appUserRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.userName").value("testuser"))
                    .andExpect(jsonPath("$.display").value("Test User"));
        }

        @Test
        @DisplayName("should return 400 for request missing username")
        void missingUserName() throws Exception {
            var request = new AppUserRequest("", "Test User");

            mockMvc.perform(post("/api/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(stringify(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.message").value("userName: must not be blank"))
                    .andExpect(jsonPath("$.path").value("/api/users"));

            verify(appUserService, never()).createAppUser(any());
        }

        @Test
        @DisplayName("should return 400 for request with duplicate username")
        void dupeUserName() throws Exception {
            when(appUserService.createAppUser(any(AppUserRequest.class)))
                    .thenThrow(new IllegalArgumentException("User with username \"testuser\" already exists"));

            mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stringify(appUserRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.message").value("User with username \"testuser\" already exists"))
                    .andExpect(jsonPath("$.path").value("/api/users"));
        }
    }

    @Nested
    @DisplayName("PUT /api/users/{id}")
    class UpdateAppUser {
        @Test
        @DisplayName("should update user")
        void happyPath() throws Exception {
            var request = new AppUserRequest("updateduser", "Updated User");

            when(appUserService.updateAppUser(eq(1L), any(AppUserRequest.class)))
                    .thenReturn(new AppUserResponse(1L, "updateduser", "Updated User"));

            mockMvc.perform(put("/api/users/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(stringify(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.userName").value("updateduser"))
                    .andExpect(jsonPath("$.display").value("Updated User"));
        }

        @Test
        @DisplayName("should return 404 when missing")
        void notFound() throws Exception {
            when(appUserService.updateAppUser(eq(999L), any(AppUserRequest.class)))
                    .thenThrow(new EntityNotFoundException("Could not find AppUser with id 999"));

            mockMvc.perform(put("/api/users/999")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(stringify(appUserRequest)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value(404))
                    .andExpect(jsonPath("$.message").value("Could not find AppUser with id 999"))
                    .andExpect(jsonPath("$.path").value("/api/users/999"));
        }

        @Test
        @DisplayName("should return 400 when request missing username")
        void missingUserName() throws Exception {
            var request = new AppUserRequest("", "Test User");

            mockMvc.perform(put("/api/users/999")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(stringify(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.message").value("userName: must not be blank"))
                    .andExpect(jsonPath("$.path").value("/api/users/999"));

            verify(appUserService, never()).updateAppUser(eq(999L), any());
        }

        @Test
        @DisplayName("should return 400 for request with duplicate username")
        void dupeUserName() throws Exception {
            when(appUserService.updateAppUser(eq(1L), any(AppUserRequest.class)))
                    .thenThrow(new IllegalArgumentException("User with username \"testuser\" already exists"));

            mockMvc.perform(put("/api/users/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(stringify(appUserRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.message").value("User with username \"testuser\" already exists"))
                    .andExpect(jsonPath("$.path").value("/api/users/1"));
        }
    }

    @Nested
    @DisplayName("DELETE /api/users/{id}")
    class DeleteAppUser {
        @Test
        @DisplayName("should delete user")
        void happyPath() throws Exception {
            mockMvc.perform(delete("/api/users/1"))
                    .andExpect(status().isOk());

            verify(appUserService).deleteAppUser(1L);
        }

        @Test
        @DisplayName("should return 404 when missing")
        void notFound() throws Exception {
            doThrow(new EntityNotFoundException("Could not find AppUser with id 999"))
                    .when(appUserService)
                    .deleteAppUser(999L);

            mockMvc.perform(delete("/api/users/999"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value(404))
                    .andExpect(jsonPath("$.message").value("Could not find AppUser with id 999"))
                    .andExpect(jsonPath("$.path").value("/api/users/999"));
        }
    }
}