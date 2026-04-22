package com.sprms.system.hbmbeans;

public enum BSAStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive");

    private final String displayName;

    BSAStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
