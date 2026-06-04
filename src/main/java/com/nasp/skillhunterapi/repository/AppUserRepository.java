package com.nasp.skillhunterapi.repository;

import com.nasp.skillhunterapi.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUserNameIgnoreCase(String userName);
    boolean existsByUserNameIgnoreCase(String userName);
}
