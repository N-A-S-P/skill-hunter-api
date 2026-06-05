package com.nasp.skillhunterapi.enums;

public enum PositionType implements LookupEnum {
    DIRECT_HIRE("Full Time"),
    CONTRACT("Contract"),
    CONTRACT_TO_HIRE("Contract-to-Hire");

    private final String display;

    PositionType(String display) {
        this.display = display;
    }

    @Override
    public String getDisplay() {
        return display;
    }
}
