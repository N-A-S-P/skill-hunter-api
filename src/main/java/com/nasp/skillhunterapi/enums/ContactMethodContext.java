package com.nasp.skillhunterapi.enums;

public enum ContactMethodContext implements LookupEnum {
    PERSONAL("Personal"),
    WORK("Work"),
    OTHER("Other");

    private final String display;

    ContactMethodContext(String display) {
        this.display = display;
    }

    @Override
    public String getDisplay() {
        return display;
    }
}
