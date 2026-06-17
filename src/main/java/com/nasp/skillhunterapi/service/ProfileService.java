package com.nasp.skillhunterapi.service;

import com.nasp.skillhunterapi.dto.Profile.AppUserRequest;
import com.nasp.skillhunterapi.dto.Profile.ProfileResponse;
import com.nasp.skillhunterapi.mapping.BaseEntityMapper;
import com.nasp.skillhunterapi.model.Profile;
import com.nasp.skillhunterapi.repository.AppUserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.oauth2.jwt.Jwt;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final AppUserRepository repository;
    private final BaseEntityMapper<Profile, ProfileResponse, AppUserRequest, AppUserRequest> mapper;

    private String getSubject() {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof Jwt jwt)) {
            throw new AccessDeniedException("No authenticated user");
        }

        return jwt.getSubject();
    }

    public Long getCurrentUserId() {
        var subject = getSubject();
        return repository.findIdByExternalSubject(subject)
                .orElseThrow(() -> new EntityNotFoundException("Could not find AppUser associated with log in"));
    }

    public Profile getCurrentUser() {
        var subject = getSubject();
        // TODO: Create AppUser from jwt if not exists for subject
        return repository.findByExternalSubject(subject)
                .orElseThrow(() -> new EntityNotFoundException("Could not find AppUser associated with log in"));
    }

    public ProfileResponse getProfile() {
        var subject = getSubject();
        var profile = repository.findByExternalSubject(subject)
                .orElseThrow(() -> new EntityNotFoundException("Could not find profile associated with this log in"));

        return mapper.toResponse(profile);
    }
}
