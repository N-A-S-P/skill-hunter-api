package com.nasp.skillhunterapi.enums;

public enum ContactMethodType implements LookupEnum {
    EMAIL("Email"),
    PHONE("Phone"),
    LINKED_IN("LinkedIn"),
    WEBSITE("Website");

    private final String display;

    ContactMethodType(String display) {
        this.display = display;
    }

    @Override
    public String getDisplay() {
        return display;
    }
}
