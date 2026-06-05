package com.nasp.skillhunterapi.enums;

public enum RelationshipType implements LookupEnum {
    COWORKER("Coworker"),
    RECRUITER("Recruiter"),
    INTERVIEWER("Interviewer"),
    HIRING_MANAGER("Hiring Manager"),
    MANAGER("Manager"),
    CLIENT("Client"),
    VENDOR("Vendor"),
    REFERENCE("Reference");

    private final String display;

    RelationshipType(String display) {
        this.display = display;
    }

    @Override
    public String getDisplay() {
        return display;
    }
}
