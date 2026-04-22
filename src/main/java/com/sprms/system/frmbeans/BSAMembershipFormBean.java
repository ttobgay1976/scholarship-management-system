package com.sprms.system.frmbeans;

import com.sprms.system.hbmbeans.FundingType;
import com.sprms.system.hbmbeans.MembershipStatus;

public class BSAMembershipFormBean {
    
    private Long membershipId;
    private Long bsaId;
    private String bsaName;
    private Long studentId;
    private String studentName;
    private String studentCid;
    private FundingType fundingType;
    private MembershipStatus membershipStatus;
    private String vpRemarks;
    private String focalRemarks;
    
    // Constructors
    public BSAMembershipFormBean() {}
    
    public BSAMembershipFormBean(Long bsaId, FundingType fundingType) {
        this.bsaId = bsaId;
        this.fundingType = fundingType;
    }
    
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
    
    public String getVpRemarks() {
        return vpRemarks;
    }
    
    public void setVpRemarks(String vpRemarks) {
        this.vpRemarks = vpRemarks;
    }
    
    public String getFocalRemarks() {
        return focalRemarks;
    }
    
    public void setFocalRemarks(String focalRemarks) {
        this.focalRemarks = focalRemarks;
    }
    
    @Override
    public String toString() {
        return "BSAMembershipFormBean{" +
                "membershipId=" + membershipId +
                ", bsaId=" + bsaId +
                ", bsaName='" + bsaName + '\'' +
                ", studentId=" + studentId +
                ", studentName='" + studentName + '\'' +
                ", fundingType=" + fundingType +
                ", membershipStatus=" + membershipStatus +
                '}';
    }
}
