package com.nasp.skillhunterapi.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EntityType {
    COMPANY("Company"),
    CONTACT("Contact"),
    INTERACTION("Interaction"),
    PLACEMENT("Placement"),
    POSITION("Position"),
    POSITION_APPLICATION("Application");

    private final String display;
}
