package com.nasp.skillhunterapi.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InteractionType {
    PHONE_CALL("Phone call"),
    EMAIL("Email"),
    ONLINE_MEETING("Online meeting"),
    IN_PERSON_MEETING("In-person meeting");

    private final String display;
}
