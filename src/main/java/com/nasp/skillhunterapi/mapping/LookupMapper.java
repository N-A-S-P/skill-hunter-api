package com.nasp.skillhunterapi.mapping;

import com.nasp.skillhunterapi.dto.LookupResponse;
import com.nasp.skillhunterapi.enums.LookupEnum;
import org.springframework.stereotype.Component;

@Component
public class LookupMapper {
    public LookupResponse toResponse(LookupEnum value) {
        return new LookupResponse(value.name(), value.getDisplay());
    }
}
