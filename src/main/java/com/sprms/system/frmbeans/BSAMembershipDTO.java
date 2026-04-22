package com.sprms.system.frmbeans;

import java.time.LocalDateTime;

import com.sprms.system.hbmbeans.FundingType;
import com.sprms.system.hbmbeans.MembershipStatus;

public class BSAMembershipDTO {
    
    private Long membershipId;
    private Long bsaId;
    private String bsaName;
    private String bsaCode;
    private Long studentId;
    private String studentName;
    private String studentCid;
    private FundingType fundingType;
    private MembershipStatus membershipStatus;
    private LocalDateTime membershipRequestDate;
    private LocalDateTime vpApprovalDate;
    private String vpApprovedBy;
    private String vpRemarks;
    private LocalDateTime focalVerificationDate;
    private String focalVerifiedBy;
    private String focalRemarks;
    private LocalDateTime createdDate;

    // Constructors
    public BSAMembershipDTO() {}

    // Getters and Setters
    public Long getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(Long membershipId) {
        this.membershipId = membershipId;
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

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentCid() {
        return studentCid;
    }

    public void setStudentCid(String studentCid) {
        this.studentCid = studentCid;
    }

    public FundingType getFundingType() {
        return fundingType;
    }

    public void setFundingType(FundingType fundingType) {
        this.fundingType = fundingType;
    }

    public MembershipStatus getMembershipStatus() {
        return membershipStatus;
    }

    public void setMembershipStatus(MembershipStatus membershipStatus) {
        this.membershipStatus = membershipStatus;
    }

    public LocalDateTime getMembershipRequestDate() {
        return membershipRequestDate;
    }

    public void setMembershipRequestDate(LocalDateTime membershipRequestDate) {
        this.membershipRequestDate = membershipRequestDate;
    }

    public LocalDateTime getVpApprovalDate() {
        return vpApprovalDate;
    }

    public void setVpApprovalDate(LocalDateTime vpApprovalDate) {
        this.vpApprovalDate = vpApprovalDate;
    }

    public String getVpApprovedBy() {
        return vpApprovedBy;
    }

    public void setVpApprovedBy(String vpApprovedBy) {
        this.vpApprovedBy = vpApprovedBy;
    }

    public String getVpRemarks() {
        return vpRemarks;
    }

    public void setVpRemarks(String vpRemarks) {
        this.vpRemarks = vpRemarks;
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

    @Override
    public String toString() {
        return "BSAMembershipDTO{" +
                "membershipId=" + membershipId +
                ", bsaName='" + bsaName + '\'' +
                ", studentName='" + studentName + '\'' +
                ", membershipStatus=" + membershipStatus +
                ", fundingType=" + fundingType +
                '}';
    }
}
