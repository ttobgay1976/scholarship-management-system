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
    
    // Additional fields for enhanced form structure
    private Long collegeId;
    private String collegeName;
    private String contactNumber;
    private String emailAddress;
    private String address;
    private String program;
    private Long indexNumber;
    private String programCourse;
    
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
    
    // Getters and Setters for new fields
    public Long getCollegeId() {
        return collegeId;
    }
    
    public void setCollegeId(Long collegeId) {
        this.collegeId = collegeId;
    }
    
    public String getCollegeName() {
        return collegeName;
    }
    
    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
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
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getProgram() {
        return program;
    }
    
    public void setProgram(String program) {
        this.program = program;
    }
    
    public Long getIndexNumber() {
        return indexNumber;
    }
    
    public void setIndexNumber(Long indexNumber) {
        this.indexNumber = indexNumber;
    }
    
    public String getProgramCourse() {
        return programCourse;
    }
    
    public void setProgramCourse(String programCourse) {
        this.programCourse = programCourse;
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
                ", collegeId=" + collegeId +
                ", collegeName='" + collegeName + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                '}';
    }
}
