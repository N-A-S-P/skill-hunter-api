package com.nasp.skillhunterapi.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WorkLocation {
    ON_SITE("On-site"),
    HYBRID("Hybrid"),
    REMOTE("Remote");

    private final String display;
}
