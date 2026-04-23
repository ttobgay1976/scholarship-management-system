package com.sprms.system.hbmbeans;

public enum FundingType {
    GOVERNMENT_FUNDED("Government Funded"),
    PRIVATELY_FUNDED("Privately Funded");

    private final String displayName;

    FundingType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
