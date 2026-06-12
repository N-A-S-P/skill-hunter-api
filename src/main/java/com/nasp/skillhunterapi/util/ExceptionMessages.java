package com.nasp.skillhunterapi.util;

import com.nasp.skillhunterapi.model.OwnedEntity;
import org.jspecify.annotations.NonNull;

public class ExceptionMessages {
    private ExceptionMessages() {}

    public static @NonNull String getEntityIdNotFoundMessage(@NonNull Class<?> entityClass, Long id) {
        return "Could not find %s with id %d"
                .formatted(entityClass.getSimpleName(), id);
    }

    public static @NonNull String getEntityIdForOwnerNotFoundMessage(@NonNull Class<? extends OwnedEntity> entityClass, long id, long ownerId) {
        return "Could not find %s with id %d for user with id %d"
                .formatted(entityClass.getSimpleName(), id, ownerId);
    }
}
