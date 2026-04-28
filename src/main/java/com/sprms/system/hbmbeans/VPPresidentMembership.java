package com.sprms.system.hbmbeans;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_vp_president_membership")
public class VPPresidentMembership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vp_president_membership_id")
    private Long vpPresidentMembershipId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_user_id", nullable = false)
    private User requester;

    @Column(name = "cid_no", nullable = false, length = 50)
    private String requesterCid;

    @Column(name = "requester_cid", nullable = false, length = 50)
    private String requesterCidDuplicate;

    @Column(name = "requester_name", nullable = false, length = 200)
    private String requesterName;

    @Column(name = "requester_email", nullable = false, length = 100)
    private String requesterEmail;

    @Column(name = "contact_number", nullable = false, length = 20)
    private String contactNumber;

    @Column(name = "requested_position", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private Position requestedPosition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bsa_id", nullable = false)
    private BSA bsa;

    @Column(name = "request_reason", nullable = false, length = 1000)
    private String requestReason;

    @Column(name = "experience_details", length = 2000)
    private String experienceDetails;

    @Column(name = "document_path", length = 500)
    private String documentPath;

    @Column(name = "request_date", nullable = false)
    private LocalDateTime requestDate;

    @Column(name = "membership_status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private VPMembershipStatus membershipStatus;

    @Column(name = "focal_verification_date")
    private LocalDateTime focalVerificationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "focal_verified_by")
    private User focalVerifiedBy;

    @Column(name = "focal_remarks", length = 1000)
    private String focalRemarks;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    // Constructors
    public VPPresidentMembership() {
        this.membershipStatus = VPMembershipStatus.PENDING;
        this.createdDate = LocalDateTime.now();
    }

    public VPPresidentMembership(User requester, Position requestedPosition, BSA bsa, String requestReason) {
        this();
        this.requester = requester;
        String cidValue = requester.getCidno();
        System.out.println("DEBUG: User CID in constructor: " + cidValue);
        this.requesterCid = cidValue;
        this.requesterCidDuplicate = cidValue; // Set both fields to the same value
        System.out.println("DEBUG: After setting - requesterCid: " + this.requesterCid + ", requesterCidDuplicate: " + this.requesterCidDuplicate);
        this.requesterName = requester.getFirstname() + " " + requester.getLastname();
        this.requesterEmail = requester.getUsername();
        this.requestedPosition = requestedPosition;
        this.bsa = bsa;
        this.requestReason = requestReason;
        this.requestDate = LocalDateTime.now();
        this.createdBy = requester.getFirstname() + " " + requester.getLastname();
    }

    // Getters and Setters
    public Long getVpPresidentMembershipId() {
        return vpPresidentMembershipId;
    }

    public void setVpPresidentMembershipId(Long vpPresidentMembershipId) {
        this.vpPresidentMembershipId = vpPresidentMembershipId;
    }

    public User getRequester() {
        return requester;
    }

    public void setRequester(User requester) {
        this.requester = requester;
    }

    public String getRequesterCid() {
        return requesterCid;
    }

    public void setRequesterCid(String requesterCid) {
        this.requesterCid = requesterCid;
    }

    public String getRequesterCidDuplicate() {
        return requesterCidDuplicate;
    }

    public void setRequesterCidDuplicate(String requesterCidDuplicate) {
        this.requesterCidDuplicate = requesterCidDuplicate;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }

    public String getRequesterEmail() {
        return requesterEmail;
    }

    public void setRequesterEmail(String requesterEmail) {
        this.requesterEmail = requesterEmail;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    
    public Position getRequestedPosition() {
        return requestedPosition;
    }

    public void setRequestedPosition(Position requestedPosition) {
        this.requestedPosition = requestedPosition;
    }

    public BSA getBsa() {
        return bsa;
    }

    public void setBsa(BSA bsa) {
        this.bsa = bsa;
    }

    public String getRequestReason() {
        return requestReason;
    }

    public void setRequestReason(String requestReason) {
        this.requestReason = requestReason;
    }

    public String getExperienceDetails() {
        return experienceDetails;
    }

    public void setExperienceDetails(String experienceDetails) {
        this.experienceDetails = experienceDetails;
    }

    public String getDocumentPath() {
        return documentPath;
    }

    public void setDocumentPath(String documentPath) {
        this.documentPath = documentPath;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }

    public VPMembershipStatus getMembershipStatus() {
        return membershipStatus;
    }

    public void setMembershipStatus(VPMembershipStatus membershipStatus) {
        this.membershipStatus = membershipStatus;
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

    @Override
    public String toString() {
        return "VPPresidentMembership{" +
                "vpPresidentMembershipId=" + vpPresidentMembershipId +
                ", requester=" + (requester != null ? requester.getFirstname() : null) +
                ", requestedPosition=" + requestedPosition +
                ", bsa=" + (bsa != null ? bsa.getBsaName() : null) +
                ", membershipStatus=" + membershipStatus +
                '}';
    }
}
