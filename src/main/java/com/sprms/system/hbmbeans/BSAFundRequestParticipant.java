package com.sprms.system.hbmbeans;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_bsa_fund_request_participants")
public class BSAFundRequestParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "participant_id")
    private Long participantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fund_request_id", nullable = false)
    private BSAFundRequest fundRequest;

    @Column(name = "participant_name", nullable = false, length = 200)
    private String participantName;

    @Column(name = "participant_cid", length = 50)
    private String participantCid;

    @Column(name = "contact_number", length = 20)
    private String contactNumber;

    @Column(name = "email_address", length = 100)
    private String emailAddress;

    @Column(name = "role_designation", length = 100)
    private String roleDesignation;

    @Column(name = "college_department", length = 200)
    private String collegeDepartment;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    // Constructors
    public BSAFundRequestParticipant() {
        this.createdDate = LocalDateTime.now();
    }

    public BSAFundRequestParticipant(BSAFundRequest fundRequest, String participantName, 
                                   String participantCid, String contactNumber, String emailAddress, 
                                   String roleDesignation, String collegeDepartment) {
        this();
        this.fundRequest = fundRequest;
        this.participantName = participantName;
        this.participantCid = participantCid;
        this.contactNumber = contactNumber;
        this.emailAddress = emailAddress;
        this.roleDesignation = roleDesignation;
        this.collegeDepartment = collegeDepartment;
    }

    // Getters and Setters
    public Long getParticipantId() {
        return participantId;
    }

    public void setParticipantId(Long participantId) {
        this.participantId = participantId;
    }

    public BSAFundRequest getFundRequest() {
        return fundRequest;
    }

    public void setFundRequest(BSAFundRequest fundRequest) {
        this.fundRequest = fundRequest;
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

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    @Override
    public String toString() {
        return "BSAFundRequestParticipant{" +
                "participantId=" + participantId +
                ", participantName='" + participantName + '\'' +
                ", participantCid='" + participantCid + '\'' +
                ", roleDesignation='" + roleDesignation + '\'' +
                '}';
    }
}
