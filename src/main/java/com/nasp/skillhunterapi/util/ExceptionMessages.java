package com.nasp.skillhunterapi.util;

import org.jspecify.annotations.NonNull;

public class ExceptionMessages {
    private ExceptionMessages() {}

    public static @NonNull String getEntityIdNotFoundMessage(@NonNull Class<?> entityClass, Long id) {
        return "Could not find %s with id %d"
                .formatted(entityClass.getSimpleName(), id);
    }
}
