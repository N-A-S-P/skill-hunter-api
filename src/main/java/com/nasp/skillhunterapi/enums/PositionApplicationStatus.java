package com.nasp.skillhunterapi.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PositionApplicationStatus {
    APPLIED("Applied"),
    SCREENING("Screening"),
    INTERVIEW("Interview"),
    OFFER("Offer"),
    REJECTED("Rejected"),
    GHOSTED("Ghosted"),
    ACCEPTED("Accepted"),
    WITHDREW("Withdrew");
    private final String display;
}
