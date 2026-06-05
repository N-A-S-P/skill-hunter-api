package com.nasp.skillhunterapi.testutils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JsonSerializer {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String stringify(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
}
