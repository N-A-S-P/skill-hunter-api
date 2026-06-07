package com.nasp.skillhunterapi.enums;

public enum PositionApplicationStatus implements LookupEnum {
    APPLIED("Applied"),
    SCREENING("Screening"),
    INTERVIEW("Interview"),
    OFFER("Offer"),
    REJECTED("Rejected"),
    GHOSTED("Ghosted"),
    ACCEPTED("Accepted"),
    WITHDREW("Withdrew"),
    ARCHIVED("Archived");

    private final String display;

    PositionApplicationStatus(String display) {
        this.display = display;
    }

    @Override
    public String getDisplay() {
        return display;
    }
}
