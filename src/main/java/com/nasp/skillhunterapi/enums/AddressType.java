package com.nasp.skillhunterapi.enums;

public enum AddressType implements LookupEnum {
    HQ("Headquarters"),
    WORK_LOCATION("Office"),
    MAILING("Mailing"),
    BILLING("Billing"),
    INTERVIEW_SITE("Interview venue");

    private final String display;

    AddressType(String display) {
        this.display = display;
    }

    @Override
    public String getDisplay() {
        return display;
    }
}
