package com.nasp.skillhunterapi.controllers;

import com.nasp.skillhunterapi.dto.AppUser.AppUserResponse;
import com.nasp.skillhunterapi.dto.AppUser.AppUserRequest;
import com.nasp.skillhunterapi.service.AppUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "AppUser", description = "AppUser CRUD endpoints")
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
class AppUserController {
    private final AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @Operation(summary = "Get all AppUsers")
    @ApiResponse(
            description = "Returns AppUsers successfully",
            responseCode = "200",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = AppUserResponse.class)))
    )
    @GetMapping
    public List<AppUserResponse> getAllAppUsers() {
        return appUserService.getAllAppUsers();
    }

    @Operation(summary = "Get an AppUser by id")
    @GetMapping("/{id}")
    public AppUserResponse getAppUserById(@PathVariable Long id) {
        return appUserService.getAppUserById(id);
    }

    @Operation(summary = "Get AppUser by username")
    @GetMapping("username/{userName}")
    public AppUserResponse getAppUserByUserName(@PathVariable String userName) {
        return appUserService.getAppUserByUserName(userName);
    }

    @Operation(summary = "Create new AppUser")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AppUserResponse createAppUser(
            @Valid
            @RequestBody
            AppUserRequest request
    ) {
        return appUserService.createAppUser(request);
    }

    @PutMapping("/{id}")
    public AppUserResponse updateAppUser(
            @PathVariable
            Long id,
            @Valid
            @RequestBody
            AppUserRequest request
    ) {
        return appUserService.updateAppUser(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteAppUser(@PathVariable Long id) {
        appUserService.deleteAppUser(id);
    }
}
