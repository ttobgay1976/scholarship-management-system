package com.sprms.system.hbmbeans;

public enum Position {
    PRESIDENT("President"),
    VICE_PRESIDENT("Vice President"),
    GENERAL_SECRETARY("General Secretary"),
    TREASURER("Treasurer"),
    EXECUTIVE_MEMBER("Executive Member"),
    ADVISOR("Advisor");

    private final String displayName;

    Position(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
