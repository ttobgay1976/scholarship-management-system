package com.sprms.system.frmbeans;

import com.sprms.system.hbmbeans.RequestStatus;
import java.time.LocalDateTime;

public class BSARequestDTO {
    
    private Long requestId;
    private String bsaName;
    private Long countryId;
    private String countryName;
    private Long stateId;
    private String stateName;
    private Long cityId;
    private String cityName;
    private String presidentCid;
    private String vicePresidentCid;
    private String description;
    private String supportingDocuments;
    private RequestStatus requestStatus;
    private String submittedBy;
    private LocalDateTime submittedDate;
    private String approvedBy;
    private LocalDateTime approvedDate;
    private String remarks;
    private String approverRemark;
    
    // Constructors
    public BSARequestDTO() {}
    
    public BSARequestDTO(Long requestId, String bsaName, String countryName, String stateName, 
                       String cityName, RequestStatus requestStatus, LocalDateTime submittedDate) {
        this.requestId = requestId;
        this.bsaName = bsaName;
        this.countryName = countryName;
        this.stateName = stateName;
        this.cityName = cityName;
        this.requestStatus = requestStatus;
        this.submittedDate = submittedDate;
    }
    
    // Getters and Setters
    public Long getRequestId() {
        return requestId;
    }
    
    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }
    
    public String getBsaName() {
        return bsaName;
    }
    
    public void setBsaName(String bsaName) {
        this.bsaName = bsaName;
    }
    
    public Long getCountryId() {
        return countryId;
    }
    
    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }
    
    public String getCountryName() {
        return countryName;
    }
    
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
    
    public Long getStateId() {
        return stateId;
    }
    
    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }
    
    public String getStateName() {
        return stateName;
    }
    
    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
    
    public Long getCityId() {
        return cityId;
    }
    
    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }
    
    public String getCityName() {
        return cityName;
    }
    
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
    
    public String getPresidentCid() {
        return presidentCid;
    }
    
    public void setPresidentCid(String presidentCid) {
        this.presidentCid = presidentCid;
    }
    
    public String getVicePresidentCid() {
        return vicePresidentCid;
    }
    
    public void setVicePresidentCid(String vicePresidentCid) {
        this.vicePresidentCid = vicePresidentCid;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getSupportingDocuments() {
        return supportingDocuments;
    }
    
    public void setSupportingDocuments(String supportingDocuments) {
        this.supportingDocuments = supportingDocuments;
    }
    
    public RequestStatus getRequestStatus() {
        return requestStatus;
    }
    
    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }
    
    public String getSubmittedBy() {
        return submittedBy;
    }
    
    public void setSubmittedBy(String submittedBy) {
        this.submittedBy = submittedBy;
    }
    
    public LocalDateTime getSubmittedDate() {
        return submittedDate;
    }
    
    public void setSubmittedDate(LocalDateTime submittedDate) {
        this.submittedDate = submittedDate;
    }
    
    public String getApprovedBy() {
        return approvedBy;
    }
    
    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }
    
    public LocalDateTime getApprovedDate() {
        return approvedDate;
    }
    
    public void setApprovedDate(LocalDateTime approvedDate) {
        this.approvedDate = approvedDate;
    }
    
    public String getRemarks() {
        return remarks;
    }
    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
    public String getApproverRemark() {
        return approverRemark;
    }
    
    public void setApproverRemark(String approverRemark) {
        this.approverRemark = approverRemark;
    }
}
