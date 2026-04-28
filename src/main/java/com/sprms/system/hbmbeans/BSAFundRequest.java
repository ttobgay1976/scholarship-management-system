package com.sprms.system.hbmbeans;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "tbl_bsa_fund_request")
public class BSAFundRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fund_request_id")
    private Long fundRequestId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bsa_id", nullable = false)
    private BSA bsa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requested_by", nullable = false)
    private User requestedBy;

    @Column(name = "activity_description", nullable = false, length = 2000)
    private String activityDescription;

    @Column(name = "event_type", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @Column(name = "joint_account_details", nullable = false, length = 500)
    private String jointAccountDetails;

    @Column(name = "bank_details", nullable = false, length = 500)
    private String bankDetails;

    @Column(name = "requested_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal requestedAmount;

    @Column(name = "approved_amount", precision = 15, scale = 2)
    private BigDecimal approvedAmount;

    @Column(name = "fund_request_status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private FundRequestStatus fundRequestStatus;

    @Column(name = "request_date", nullable = false)
    private LocalDateTime requestDate;

    @Column(name = "focal_verification_date")
    private LocalDateTime focalVerificationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "focal_verified_by")
    private User focalVerifiedBy;

    @Column(name = "focal_remarks", length = 1000)
    private String focalRemarks;

    @Column(name = "chief_approval_date")
    private LocalDateTime chiefApprovalDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chief_approved_by")
    private User chiefApprovedBy;

    @Column(name = "chief_remarks", length = 1000)
    private String chiefRemarks;

    @Column(name = "accountant_approval_date")
    private LocalDateTime accountantApprovalDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountant_approved_by")
    private User accountantApprovedBy;

    @Column(name = "accountant_remarks", length = 1000)
    private String accountantRemarks;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    @Column(name = "reference_number", nullable = false, length = 20, unique = true)
    private String referenceNumber;

    // OneToMany relationships
    @OneToMany(mappedBy = "fundRequest", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<BSAFundRequestDocument> documents;

    @OneToMany(mappedBy = "fundRequest", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<BSAFundRequestParticipant> participants;

    // Constructors
    public BSAFundRequest() {
        this.fundRequestStatus = FundRequestStatus.PENDING;
        this.requestDate = LocalDateTime.now();
        this.createdDate = LocalDateTime.now();
    }

    public BSAFundRequest(BSA bsa, User requestedBy, String activityDescription, EventType eventType,
                         String jointAccountDetails, String bankDetails, BigDecimal requestedAmount) {
        this();
        this.bsa = bsa;
        this.requestedBy = requestedBy;
        this.activityDescription = activityDescription;
        this.eventType = eventType;
        this.jointAccountDetails = jointAccountDetails;
        this.bankDetails = bankDetails;
        this.requestedAmount = requestedAmount;
    }

    // Getters and Setters
    public Long getFundRequestId() {
        return fundRequestId;
    }

    public void setFundRequestId(Long fundRequestId) {
        this.fundRequestId = fundRequestId;
    }

    public BSA getBsa() {
        return bsa;
    }

    public void setBsa(BSA bsa) {
        this.bsa = bsa;
    }

    public User getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(User requestedBy) {
        this.requestedBy = requestedBy;
    }

    public String getActivityDescription() {
        return activityDescription;
    }

    public void setActivityDescription(String activityDescription) {
        this.activityDescription = activityDescription;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
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

    public FundRequestStatus getFundRequestStatus() {
        return fundRequestStatus;
    }

    public void setFundRequestStatus(FundRequestStatus fundRequestStatus) {
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

    public User getFocalVerifiedBy() {
        return focalVerifiedBy;
    }

    public void setFocalVerifiedBy(User focalVerifiedBy) {
        this.focalVerifiedBy = focalVerifiedBy;
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

    public User getChiefApprovedBy() {
        return chiefApprovedBy;
    }

    public void setChiefApprovedBy(User chiefApprovedBy) {
        this.chiefApprovedBy = chiefApprovedBy;
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

    public User getAccountantApprovedBy() {
        return accountantApprovedBy;
    }

    public void setAccountantApprovedBy(User accountantApprovedBy) {
        this.accountantApprovedBy = accountantApprovedBy;
    }

    public String getAccountantRemarks() {
        return accountantRemarks;
    }

    public void setAccountantRemarks(String accountantRemarks) {
        this.accountantRemarks = accountantRemarks;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public java.util.List<BSAFundRequestDocument> getDocuments() {
        return documents;
    }

    public void setDocuments(java.util.List<BSAFundRequestDocument> documents) {
        this.documents = documents;
    }

    public java.util.List<BSAFundRequestParticipant> getParticipants() {
        return participants;
    }

    public void setParticipants(java.util.List<BSAFundRequestParticipant> participants) {
        this.participants = participants;
    }

    @Override
    public String toString() {
        return "BSAFundRequest{" +
                "fundRequestId=" + fundRequestId +
                ", bsa=" + (bsa != null ? bsa.getBsaName() : null) +
                ", requestedBy=" + (requestedBy != null ? requestedBy.getFirstname() : null) +
                ", fundRequestStatus=" + fundRequestStatus +
                ", requestedAmount=" + requestedAmount +
                '}';
    }
}
