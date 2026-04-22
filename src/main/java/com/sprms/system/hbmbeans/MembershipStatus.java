package com.sprms.system.hbmbeans;

public enum MembershipStatus {
    PENDING("Pending"),
    APPROVED("Approved"),
    REJECTED("Rejected"),
    VERIFIED("Verified");

    private final String displayName;

    MembershipStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
