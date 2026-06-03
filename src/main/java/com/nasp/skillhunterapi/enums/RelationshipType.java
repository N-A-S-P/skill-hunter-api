package com.nasp.skillhunterapi.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RelationshipType {
    COWORKER("Coworker"),
    RECRUITER("Recruiter"),
    INTERVIEWER("Interviewer"),
    HIRING_MANAGER("Hiring Manager"),
    MANAGER("Manager"),
    CLIENT("Client"),
    VENDOR("Vendor"),
    REFERENCE("Reference");

    private final String display;
}
