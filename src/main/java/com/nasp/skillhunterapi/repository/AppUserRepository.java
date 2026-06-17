package com.nasp.skillhunterapi.repository;

import com.nasp.skillhunterapi.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByUserNameIgnoreCase(String userName);
    boolean existsByUserNameIgnoreCase(String userName);
    Optional<Profile> findByExternalSubject(String subject);
    Optional<Long> findIdByExternalSubject(String subject);
}
