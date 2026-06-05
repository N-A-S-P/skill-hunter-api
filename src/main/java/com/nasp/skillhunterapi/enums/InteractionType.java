package com.nasp.skillhunterapi.enums;

public enum InteractionType implements LookupEnum {
    PHONE_CALL("Phone call"),
    EMAIL("Email"),
    ONLINE_MEETING("Online meeting"),
    IN_PERSON_MEETING("In-person meeting");

    private final String display;

    InteractionType(String display) {
        this.display = display;
    }

    @Override
    public String getDisplay() {
        return display;
    }
}
