package com.nasp.skillhunterapi.repository;

import com.nasp.skillhunterapi.model.OwnedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
interface OwnedEntityRepository<T extends OwnedEntity, ID> extends JpaRepository<T, ID> {
    Optional<T> findByIdAndOwnerId(ID id, Long ownerId);
    List<T> findAllByOwnerId(Long ownerId);
}
