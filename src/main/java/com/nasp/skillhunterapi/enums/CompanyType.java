package com.nasp.skillhunterapi.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CompanyType {
    RECRUITING_AGENCY("Recruiting Agency"),
    STAFFING_FIRM("Staffing Firm"),
    HIRER("Hirer");

    private final String display;
}
