package com.nasp.skillhunterapi.controllers;

import com.nasp.skillhunterapi.dto.Profile.ProfileResponse;
import com.nasp.skillhunterapi.service.ProfileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@Tag(name = "Profile")
@RequestMapping(value = "/api/me", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
class ProfileController {
    private final ProfileService service;

    @GetMapping
    public ProfileResponse getCurrentUser() {
        return service.getProfile();
    }
}
