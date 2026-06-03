package com.nasp.skillhunterapi.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AddressType {
    HQ("Headquarters"),
    WORK_LOCATION("Office"),
    MAILING("Mailing"),
    BILLING("Billing"),
    INTERVIEW_SITE("Interview venue");

    private final String display;
}
