package com.nasp.skillhunterapi.controllers;

import com.nasp.skillhunterapi.dto.AppUserDto;
import com.nasp.skillhunterapi.dto.AppUserRequest;
import com.nasp.skillhunterapi.service.AppUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "AppUser")
@RequestMapping("/api/users")
class AppUserController {
    private final AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping
    public List<AppUserDto> getAllAppUsers() {
        return appUserService.getAllAppUsers();
    }

    @GetMapping("/{id}")
    public AppUserDto getAppUserById(@PathVariable Long id) {
        return appUserService.getAppUserById(id);
    }

    @GetMapping("username/{userName}")
    public AppUserDto getAppUserByUserName(@PathVariable String userName) {
        return appUserService.getAppUserByUserName(userName);
    }

    @PostMapping
    public AppUserDto createAppUser(
            @Valid
            @RequestBody
            AppUserRequest request
    ) {
        return appUserService.createAppUser(request);
    }

    @PutMapping("/{id}")
    public AppUserDto updateAppUser(
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
