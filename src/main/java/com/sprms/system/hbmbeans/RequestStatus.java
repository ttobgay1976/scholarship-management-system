package com.sprms.system.hbmbeans;

public enum RequestStatus {
    PENDING("Pending"),
    APPROVED("Approved"),
    REJECTED("Rejected"),
    UNDER_REVIEW("Under Review");
    
    private final String displayName;
    
    RequestStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
