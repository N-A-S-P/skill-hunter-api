package com.nasp.skillhunterapi.mapping;

import com.nasp.skillhunterapi.model.AppUser;
import com.nasp.skillhunterapi.model.OwnedEntity;

public interface OwnedEntityMapper<
        ENTITY extends OwnedEntity,
        DETAIL_RESPONSE,
        LIST_ITEM_RESPONSE,
        CREATE_REQUEST,
        UPDATE_REQUEST> {
    DETAIL_RESPONSE toDetailResponse(ENTITY entity);

    LIST_ITEM_RESPONSE toListItemResponse(ENTITY entity);

    ENTITY toEntity(CREATE_REQUEST request, AppUser owner);

    void updateEntity(ENTITY entity, UPDATE_REQUEST request);
}
