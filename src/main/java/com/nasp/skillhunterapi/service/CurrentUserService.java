package com.nasp.skillhunterapi.service;

import com.nasp.skillhunterapi.model.AppUser;
import com.nasp.skillhunterapi.repository.AppUserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.nasp.skillhunterapi.util.ExceptionMessages.getEntityIdNotFoundMessage;

@Service
@RequiredArgsConstructor
class CurrentUserService {
    private final AppUserRepository repository;
    public Long getCurrentUserId() {
        return 1L;
    }

    public AppUser getCurrentUser() {
        return repository.findById(1L)
                .orElseThrow(() -> new EntityNotFoundException(getEntityIdNotFoundMessage(AppUser.class, 1L)));
    }
}
