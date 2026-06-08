package com.nasp.skillhunterapi.service;

import com.nasp.skillhunterapi.dto.AppUser.AppUserResponse;
import com.nasp.skillhunterapi.dto.AppUser.AppUserRequest;
import com.nasp.skillhunterapi.mapping.Mapper;
import com.nasp.skillhunterapi.model.AppUser;
import com.nasp.skillhunterapi.repository.AppUserRepository;
import static com.nasp.skillhunterapi.util.ExceptionMessages.getEntityIdNotFoundMessage;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AppUserService {
    private final AppUserRepository repository;
    private final Mapper<AppUser, AppUserResponse, AppUserRequest, AppUserRequest> mapper;

    public List<AppUserResponse> getAllAppUsers() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public AppUserResponse getAppUserById(Long id) {
        var user = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        getEntityIdNotFoundMessage(AppUser.class, id)
                ));
        return mapper.toResponse(user);
    }

    public AppUserResponse getAppUserByUserName(String userName) {
        var user = repository.findByUserNameIgnoreCase(userName)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Could not find AppUser with username \"%s\"".formatted(userName)));
        return mapper.toResponse(user);
    }

    public AppUserResponse createAppUser(AppUserRequest request) {
        if (repository.existsByUserNameIgnoreCase(request.userName())) {
            throw new IllegalArgumentException(getDuplicateUserNameMessage(request.userName()));
        }

        var user = repository.save(mapper.toEntity(request));
        return mapper.toResponse(user);
    }

    public AppUserResponse updateAppUser(Long id, AppUserRequest request) {
        var existingUser = repository.findByUserNameIgnoreCase(request.userName());
        if (existingUser.isPresent() && !existingUser.get().getId().equals(id)) {
            throw new IllegalArgumentException(getDuplicateUserNameMessage(request.userName()));
        }

        var user = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        getEntityIdNotFoundMessage(AppUser.class, id)));
        mapper.updateEntity(user, request);
        repository.save(user);
        return mapper.toResponse(user);
    }

    public void deleteAppUser(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException(getEntityIdNotFoundMessage(AppUser.class, id));
        }

        repository.deleteById(id);
    }

    private String getDuplicateUserNameMessage(String userName) {
        return "User with username \"%s\" already exists".formatted(userName);
    }
}
