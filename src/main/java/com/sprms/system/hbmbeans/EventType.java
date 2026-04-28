package com.sprms.system.hbmbeans;

public enum EventType {
    SPORTS_COMPETITION("Sports Competition"),
    CULTURAL_FESTIVAL("Cultural Festival"),
    WORKSHOP_SEMINAR("Workshop/Seminar"),
    COMMUNITY_SERVICE("Community Service"),
    TRAINING_PROGRAM("Training Program"),
    EXCURSION_TOUR("Excursion/Tour"),
    CLUB_MEETING("Club Meeting"),
    GRADUATION_CEREMONY("Graduation Ceremony"),
    OTHER("Other");

    private final String displayName;

    EventType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
