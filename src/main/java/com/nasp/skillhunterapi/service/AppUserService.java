package com.nasp.skillhunterapi.service;

import com.nasp.skillhunterapi.dto.AppUserDto;
import com.nasp.skillhunterapi.dto.AppUserRequest;
import com.nasp.skillhunterapi.mapping.AppUserMapper;
import com.nasp.skillhunterapi.repository.AppUserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserService {
    private final AppUserRepository repository;
    private final AppUserMapper mapper;

    public AppUserService(AppUserRepository repository, AppUserMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<AppUserDto> getAllAppUsers() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public AppUserDto getAppUserById(Long id) {
        var user = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Could not find AppUser with id %d".formatted(id)));
        return mapper.toDto(user);
    }

    public AppUserDto getAppUserByUserName(String userName) {
        var user = repository.findByUserName(userName)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Could not find AppUser with username \"%s\"".formatted(userName)));
        return mapper.toDto(user);
    }

    public AppUserDto createAppUser(AppUserRequest request) {
        if (!repository.existsByUserName(request.userName())) {
            var user = repository.save(mapper.toEntity(request));
            return mapper.toDto(user);
        } else {
            throw new IllegalArgumentException("User with username \"%s\" already exists".formatted(request.userName()));
        }
    }

    public AppUserDto updateAppUser(Long id, AppUserRequest request) {
        var user = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Could not find AppUser with id %d".formatted(id)));
        mapper.updateEntity(user, request);
        repository.save(user);
        return mapper.toDto(user);
    }

    public void deleteAppUser(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        }
        else {
            throw new EntityNotFoundException("Could not find AppUser with id %d".formatted(id));
        }
    }
}
