package com.nasp.skillhunterapi.enums;

public enum EntityType implements LookupEnum {
    COMPANY("Company"),
    CONTACT("Contact"),
    INTERACTION("Interaction"),
    PLACEMENT("Placement"),
    POSITION("Position"),
    POSITION_APPLICATION("Application");

    private final String display;

    EntityType(String display) {
        this.display = display;
    }

    @Override
    public String getDisplay() {
        return display;
    }
}
