package com.nasp.skillhunterapi.testutils;

import com.nasp.skillhunterapi.dto.LookupResponse;
import com.nasp.skillhunterapi.enums.LookupEnum;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

public final class LookupResponseAssertions {
    private LookupResponseAssertions() {}

    public static Consumer<LookupResponse> matchesLookup(LookupEnum expected) {
        return actual -> {
            assertThat(actual.value()).isEqualTo(expected.name());
            assertThat(actual.display()).isEqualTo(expected.getDisplay());
        };
    }
}
