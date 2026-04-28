package com.sprms.system.frmbeans;

import java.time.LocalDateTime;

public class BSAFundRequestParticipantDTO {
    
    private Long participantId;
    private String participantName;
    private String participantCid;
    private String contactNumber;
    private String emailAddress;
    private String roleDesignation;
    private String collegeDepartment;
    private LocalDateTime createdDate;

    // Constructors
    public BSAFundRequestParticipantDTO() {}

    // Getters and Setters
    public Long getParticipantId() {
        return participantId;
    }

    public void setParticipantId(Long participantId) {
        this.participantId = participantId;
    }

    public String getParticipantName() {
        return participantName;
    }

    public void setParticipantName(String participantName) {
        this.participantName = participantName;
    }

    public String getParticipantCid() {
        return participantCid;
    }

    public void setParticipantCid(String participantCid) {
        this.participantCid = participantCid;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getRoleDesignation() {
        return roleDesignation;
    }

    public void setRoleDesignation(String roleDesignation) {
        this.roleDesignation = roleDesignation;
    }

    public String getCollegeDepartment() {
        return collegeDepartment;
    }

    public void setCollegeDepartment(String collegeDepartment) {
        this.collegeDepartment = collegeDepartment;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}
