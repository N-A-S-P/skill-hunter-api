package com.nasp.skillhunterapi.service;

import com.nasp.skillhunterapi.dto.Profile.ProfileResponse;
import com.nasp.skillhunterapi.dto.Profile.AppUserRequest;
import com.nasp.skillhunterapi.mapping.BaseEntityMapper;
import com.nasp.skillhunterapi.model.Profile;
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
    private final BaseEntityMapper<Profile, ProfileResponse, AppUserRequest, AppUserRequest> userMapper;

    public List<ProfileResponse> getAllAppUsers() {
        return repository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    public ProfileResponse getAppUserById(Long id) {
        var user = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        getEntityIdNotFoundMessage(Profile.class, id)
                ));
        return userMapper.toResponse(user);
    }

    public ProfileResponse getAppUserByUserName(String userName) {
        var user = repository.findByUserNameIgnoreCase(userName)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Could not find AppUser with username \"%s\"".formatted(userName)));
        return userMapper.toResponse(user);
    }

    public ProfileResponse createAppUser(AppUserRequest request) {
        if (repository.existsByUserNameIgnoreCase(request.userName())) {
            throw new IllegalArgumentException(getDuplicateUserNameMessage(request.userName()));
        }

        var user = repository.save(userMapper.toEntity(request));
        return userMapper.toResponse(user);
    }

    public ProfileResponse updateAppUser(Long id, AppUserRequest request) {
        var existingUser = repository.findByUserNameIgnoreCase(request.userName());
        if (existingUser.isPresent() && !existingUser.get().getId().equals(id)) {
            throw new IllegalArgumentException(getDuplicateUserNameMessage(request.userName()));
        }

        var user = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        getEntityIdNotFoundMessage(Profile.class, id)));
        userMapper.updateEntity(user, request);
        repository.save(user);
        return userMapper.toResponse(user);
    }

    public void deleteAppUser(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException(getEntityIdNotFoundMessage(Profile.class, id));
        }

        repository.deleteById(id);
    }

    private String getDuplicateUserNameMessage(String userName) {
        return "User with username \"%s\" already exists".formatted(userName);
    }
}
