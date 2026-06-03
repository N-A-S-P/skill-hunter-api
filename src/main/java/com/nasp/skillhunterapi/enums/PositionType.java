package com.nasp.skillhunterapi.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PositionType {
    DIRECT_HIRE("Full Time"),
    CONTRACT("Contract"),
    CONTRACT_TO_HIRE("Contract-to-Hire");

    private final String display;
}
