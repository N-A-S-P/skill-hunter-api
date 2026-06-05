package com.nasp.skillhunterapi.service;

import com.nasp.skillhunterapi.dto.AppUserDto;
import com.nasp.skillhunterapi.dto.AppUserRequest;
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
    private final Mapper<AppUser, AppUserDto, AppUserRequest, AppUserRequest> mapper;

    public List<AppUserDto> getAllAppUsers() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public AppUserDto getAppUserById(Long id) {
        var user = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        getEntityIdNotFoundMessage(AppUser.class, id)
                ));
        return mapper.toDto(user);
    }

    public AppUserDto getAppUserByUserName(String userName) {
        var user = repository.findByUserNameIgnoreCase(userName)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Could not find AppUser with username \"%s\"".formatted(userName)));
        return mapper.toDto(user);
    }

    public AppUserDto createAppUser(AppUserRequest request) {
        if (repository.existsByUserNameIgnoreCase(request.userName())) {
            throw new IllegalArgumentException(getDuplicateUserNameMessage(request.userName()));
        }

        var user = repository.save(mapper.toEntity(request));
        return mapper.toDto(user);
    }

    public AppUserDto updateAppUser(Long id, AppUserRequest request) {
        var existingUser = repository.findByUserNameIgnoreCase(request.userName());
        if (existingUser.isPresent() && !existingUser.get().getId().equals(id)) {
            throw new IllegalArgumentException(getDuplicateUserNameMessage(request.userName()));
        }

        var user = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        getEntityIdNotFoundMessage(AppUser.class, id)));
        mapper.updateEntity(user, request);
        repository.save(user);
        return mapper.toDto(user);
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
