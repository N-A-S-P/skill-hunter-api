package com.nasp.skillhunterapi.enums;

public enum WorkLocation implements LookupEnum {
    ON_SITE("On-site"),
    HYBRID("Hybrid"),
    REMOTE("Remote");

    private final String display;

    WorkLocation(String display) {
        this.display = display;
    }

    @Override
    public String getDisplay() {
        return display;
    }
}
