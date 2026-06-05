package com.nasp.skillhunterapi.enums;

public enum CompanyType implements LookupEnum {
    RECRUITING_AGENCY("Recruiting Agency"),
    STAFFING_FIRM("Staffing Firm"),
    HIRER("Hirer");

    private final String display;

    CompanyType(String display) {
        this.display = display;
    }

    @Override
    public String getDisplay() {
        return display;
    }
}
