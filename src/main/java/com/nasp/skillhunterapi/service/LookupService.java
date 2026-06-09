package com.nasp.skillhunterapi.service;

import com.nasp.skillhunterapi.dto.LookupResponse;
import com.nasp.skillhunterapi.enums.*;
import com.nasp.skillhunterapi.mapping.LookupMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class LookupService {
    private final Map<String, Class<? extends LookupEnum>> lookupTypes =
            Map.of(
                    "addressType", AddressType.class,
                    "applicationStatus", PositionApplicationStatus.class,
                    "companyType", CompanyType.class,
                    "interactionType", InteractionType.class,
                    "positionType", PositionType.class,
                    "relationshipType", RelationshipType.class,
                    "workLocation", WorkLocation.class
            );

    private final LookupMapper lookupMapper;

    public LookupService(LookupMapper lookupMapper) {
        this.lookupMapper = lookupMapper;
    }

    public List<LookupResponse> getLookup(String lookupType) {
        if (lookupType == null || lookupType.isBlank()) {
            throw new IllegalArgumentException("lookup type must not be null, empty or all whitespace");
        }

        var enumType = lookupTypes.get(lookupType);

        if (enumType == null) {
            throw new EntityNotFoundException("%s lookup type does not exist".formatted(lookupType));
        }

        return Arrays.stream(enumType.getEnumConstants())
                .map(lookupMapper::toResponse).toList();
    }

}
