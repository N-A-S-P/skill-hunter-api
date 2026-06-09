package com.nasp.skillhunterapi.mapping;

import com.nasp.skillhunterapi.model.BaseEntity;

public interface BaseEntityMapper<ENTITY extends BaseEntity, RESPONSE, CREATE_REQUEST, UPDATE_REQUEST> {
    RESPONSE toResponse(ENTITY entity);
    ENTITY toEntity(CREATE_REQUEST request);
    void updateEntity(ENTITY entity, UPDATE_REQUEST request);
}
