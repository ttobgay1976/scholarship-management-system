package com.sprms.system.frmbeans;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

public class BSAFundRequestDTO {
    
    private Long fundRequestId;
    private String bsaName;
    private String bsaCode;
    private String requestedByName;
    private String requestedByCid;
    private String referenceNumber;
    private String activityDescription;
    private String eventType;
    private String jointAccountDetails;
    private String bankDetails;
    private BigDecimal requestedAmount;
    private BigDecimal approvedAmount;
    private String fundRequestStatus;
    private LocalDateTime requestDate;
    private LocalDateTime focalVerificationDate;
    private String focalVerifiedByName;
    private String focalRemarks;
    private LocalDateTime chiefApprovalDate;
    private String chiefApprovedByName;
    private String chiefRemarks;
    private LocalDateTime accountantApprovalDate;
    private String accountantApprovedByName;
    private String accountantRemarks;
    private List<BSAFundRequestDocumentDTO> documents;
    private List<BSAFundRequestParticipantDTO> participants;

    // Constructors
    public BSAFundRequestDTO() {}

    // Getters and Setters
    public Long getFundRequestId() {
        return fundRequestId;
    }

    public void setFundRequestId(Long fundRequestId) {
        this.fundRequestId = fundRequestId;
    }

    public String getBsaName() {
        return bsaName;
    }

    public void setBsaName(String bsaName) {
        this.bsaName = bsaName;
    }

    public String getBsaCode() {
        return bsaCode;
    }

    public void setBsaCode(String bsaCode) {
        this.bsaCode = bsaCode;
    }

    public String getRequestedByName() {
        return requestedByName;
    }

    public void setRequestedByName(String requestedByName) {
        this.requestedByName = requestedByName;
    }

    public String getRequestedByCid() {
        return requestedByCid;
    }

    public void setRequestedByCid(String requestedByCid) {
        this.requestedByCid = requestedByCid;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getActivityDescription() {
        return activityDescription;
    }

    public void setActivityDescription(String activityDescription) {
        this.activityDescription = activityDescription;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getJointAccountDetails() {
        return jointAccountDetails;
    }

    public void setJointAccountDetails(String jointAccountDetails) {
        this.jointAccountDetails = jointAccountDetails;
    }

    public String getBankDetails() {
        return bankDetails;
    }

    public void setBankDetails(String bankDetails) {
        this.bankDetails = bankDetails;
    }

    public BigDecimal getRequestedAmount() {
        return requestedAmount;
    }

    public void setRequestedAmount(BigDecimal requestedAmount) {
        this.requestedAmount = requestedAmount;
    }

    public BigDecimal getApprovedAmount() {
        return approvedAmount;
    }

    public void setApprovedAmount(BigDecimal approvedAmount) {
        this.approvedAmount = approvedAmount;
    }

    public String getFundRequestStatus() {
        return fundRequestStatus;
    }

    public void setFundRequestStatus(String fundRequestStatus) {
        this.fundRequestStatus = fundRequestStatus;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }

    public LocalDateTime getFocalVerificationDate() {
        return focalVerificationDate;
    }

    public void setFocalVerificationDate(LocalDateTime focalVerificationDate) {
        this.focalVerificationDate = focalVerificationDate;
    }

    public String getFocalVerifiedByName() {
        return focalVerifiedByName;
    }

    public void setFocalVerifiedByName(String focalVerifiedByName) {
        this.focalVerifiedByName = focalVerifiedByName;
    }

    public String getFocalRemarks() {
        return focalRemarks;
    }

    public void setFocalRemarks(String focalRemarks) {
        this.focalRemarks = focalRemarks;
    }

    public LocalDateTime getChiefApprovalDate() {
        return chiefApprovalDate;
    }

    public void setChiefApprovalDate(LocalDateTime chiefApprovalDate) {
        this.chiefApprovalDate = chiefApprovalDate;
    }

    public String getChiefApprovedByName() {
        return chiefApprovedByName;
    }

    public void setChiefApprovedByName(String chiefApprovedByName) {
        this.chiefApprovedByName = chiefApprovedByName;
    }

    public String getChiefRemarks() {
        return chiefRemarks;
    }

    public void setChiefRemarks(String chiefRemarks) {
        this.chiefRemarks = chiefRemarks;
    }

    public LocalDateTime getAccountantApprovalDate() {
        return accountantApprovalDate;
    }

    public void setAccountantApprovalDate(LocalDateTime accountantApprovalDate) {
        this.accountantApprovalDate = accountantApprovalDate;
    }

    public String getAccountantApprovedByName() {
        return accountantApprovedByName;
    }

    public void setAccountantApprovedByName(String accountantApprovedByName) {
        this.accountantApprovedByName = accountantApprovedByName;
    }

    public String getAccountantRemarks() {
        return accountantRemarks;
    }

    public void setAccountantRemarks(String accountantRemarks) {
        this.accountantRemarks = accountantRemarks;
    }

    public List<BSAFundRequestDocumentDTO> getDocuments() {
        return documents;
    }

    public void setDocuments(List<BSAFundRequestDocumentDTO> documents) {
        this.documents = documents;
    }

    public List<BSAFundRequestParticipantDTO> getParticipants() {
        return participants;
    }

    public void setParticipants(List<BSAFundRequestParticipantDTO> participants) {
        this.participants = participants;
    }
}
