package com.sprms.system.frmbeans;

import java.time.LocalDateTime;

import com.sprms.system.hbmbeans.Position;
import com.sprms.system.hbmbeans.VPMembershipStatus;

public class VPPresidentMembershipDTO {
    private Long vpPresidentMembershipId;
    private Long requesterId;
    private String requesterName;
    private String requesterCid;
    private String requesterEmail;
    private String contactNumber;
    private String cidNo;
    private Position requestedPosition;
    private Long bsaId;
    private String bsaName;
    private String bsaCode;
    private String requestReason;
    private String experienceDetails;
    private String documentPath;
    private LocalDateTime requestDate;
    private VPMembershipStatus membershipStatus;
    private LocalDateTime focalVerificationDate;
    private String focalVerifiedBy;
    private String focalRemarks;
    private LocalDateTime createdDate;
    private String createdBy;

    // Getters and Setters
    public Long getVpPresidentMembershipId() {
        return vpPresidentMembershipId;
    }

    public void setVpPresidentMembershipId(Long vpPresidentMembershipId) {
        this.vpPresidentMembershipId = vpPresidentMembershipId;
    }

    public Long getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(Long requesterId) {
        this.requesterId = requesterId;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }

    public String getRequesterCid() {
        return requesterCid;
    }

    public void setRequesterCid(String requesterCid) {
        this.requesterCid = requesterCid;
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

    public String getCidNo() {
        return cidNo;
    }

    public void setCidNo(String cidNo) {
        this.cidNo = cidNo;
    }

    public Position getRequestedPosition() {
        return requestedPosition;
    }

    public void setRequestedPosition(Position requestedPosition) {
        this.requestedPosition = requestedPosition;
    }

    public Long getBsaId() {
        return bsaId;
    }

    public void setBsaId(Long bsaId) {
        this.bsaId = bsaId;
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

    public String getFocalVerifiedBy() {
        return focalVerifiedBy;
    }

    public void setFocalVerifiedBy(String focalVerifiedBy) {
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
