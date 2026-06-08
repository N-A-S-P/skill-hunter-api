package com.nasp.skillhunterapi.mapping;

import com.nasp.skillhunterapi.model.BaseEntity;

public interface Mapper<T extends BaseEntity, DTO, CREATE, UPDATE> {
    DTO toResponse(T entity);
    T toEntity(CREATE request);
    void updateEntity(T entity, UPDATE request);
}
