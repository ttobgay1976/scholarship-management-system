package com.sprms.system.hbmbeans;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_bsa_membership")
public class BSAMembership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "membership_id")
    private Long membershipId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bsa_id", nullable = false)
    private BSA bsa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @Column(name = "student_cid", nullable = false, length = 50)
    private String studentCid;

    @Column(name = "college_name", nullable = false, length = 200)
    private String college_Name;

    @Column(name = "student_email", nullable = false, length = 100)
    private String studentEmail;

    @Column(name = "student_name", nullable = false, length = 200)
    private String studentName;

    @Column(name = "requested_date", nullable = false)
    private LocalDateTime requestedDate;

    @Column(name = "membership_request_date", nullable = false)
    private LocalDateTime membershipRequestDate;

    @Column(name = "funding_type", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private FundingType fundingType;

    @Column(name = "membership_status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private MembershipStatus membershipStatus;

    @Column(name = "vp_approval_date")
    private LocalDateTime vpApprovalDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vp_approved_by")
    private User vpApprovedBy;

    @Column(name = "vp_remarks", length = 1000)
    private String vpRemarks;

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
    public BSAMembership() {
        this.membershipStatus = MembershipStatus.PENDING;
        this.createdDate = LocalDateTime.now();
    }

    public BSAMembership(BSA bsa, User student, FundingType fundingType) {
        this();
        this.bsa = bsa;
        this.student = student;
        this.fundingType = fundingType;
        this.membershipRequestDate = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getMembershipId() {
        return membershipId;
    }

    public String getCollege_Name() {
        return college_Name;
    }

    public void setCollege_Name(String college_Name) {
        this.college_Name = college_Name;
    }

    public void setMembershipId(Long membershipId) {
        this.membershipId = membershipId;
    }

    public BSA getBsa() {
        return bsa;
    }

    public void setBsa(BSA bsa) {
        this.bsa = bsa;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public String getStudentCid() {
        return studentCid;
    }

    public void setStudentCid(String studentCid) {
        this.studentCid = studentCid;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public LocalDateTime getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(LocalDateTime requestedDate) {
        this.requestedDate = requestedDate;
    }

    public LocalDateTime getMembershipRequestDate() {
        return membershipRequestDate;
    }

    public void setMembershipRequestDate(LocalDateTime membershipRequestDate) {
        this.membershipRequestDate = membershipRequestDate;
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

    public LocalDateTime getVpApprovalDate() {
        return vpApprovalDate;
    }

    public void setVpApprovalDate(LocalDateTime vpApprovalDate) {
        this.vpApprovalDate = vpApprovalDate;
    }

    public User getVpApprovedBy() {
        return vpApprovedBy;
    }

    public void setVpApprovedBy(User vpApprovedBy) {
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
        return "BSAMembership{" +
                "membershipId=" + membershipId +
                ", bsa=" + (bsa != null ? bsa.getBsaName() : null) +
                ", student=" + (student != null ? student.getFirstname() : null) +
                ", membershipStatus=" + membershipStatus +
                ", fundingType=" + fundingType +
                '}';
    }
}
