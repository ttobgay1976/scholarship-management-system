package com.sprms.system.frmbeans;

import java.time.LocalDateTime;
import java.util.List;

public class BSACollegeRegistrationDTO {

    private Long registrationId;
    private Long bsaId;
    private String bsaCode;
    private String bsaName;
    
    private Long collegeId;
    private String collegeName;
    private String collegeShortName;
    
    private Long countryId;
    private String countryName;
    
    private Long stateId;
    private String stateName;
    
    private Long cityId;
    private String cityName;
    
    private LocalDateTime registrationDate;
    private String status;
    private String remarks;
    
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private String createdBy;
    private String updatedBy;
    
    // For multi-select college registration
    private List<Long> selectedCollegeIds;
    private List<CollegeInfo> availableColleges;
    
    public static class CollegeInfo {
        private Long collegeId;
        private String collegeName;
        private String shortName;
        private boolean selected;
        
        public CollegeInfo() {}
        
        public CollegeInfo(Long collegeId, String collegeName, String shortName) {
            this.collegeId = collegeId;
            this.collegeName = collegeName;
            this.shortName = shortName;
            this.selected = false;
        }
        
        // Getters and Setters
        public Long getCollegeId() { return collegeId; }
        public void setCollegeId(Long collegeId) { this.collegeId = collegeId; }
        
        public String getCollegeName() { return collegeName; }
        public void setCollegeName(String collegeName) { this.collegeName = collegeName; }
        
        public String getShortName() { return shortName; }
        public void setShortName(String shortName) { this.shortName = shortName; }
        
        public boolean isSelected() { return selected; }
        public void setSelected(boolean selected) { this.selected = selected; }
    }

    // Constructors
    public BSACollegeRegistrationDTO() {}

    // Getters and Setters
    public Long getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(Long registrationId) {
        this.registrationId = registrationId;
    }

    public Long getBsaId() {
        return bsaId;
    }

    public void setBsaId(Long bsaId) {
        this.bsaId = bsaId;
    }

    public String getBsaCode() {
        return bsaCode;
    }

    public void setBsaCode(String bsaCode) {
        this.bsaCode = bsaCode;
    }

    public String getBsaName() {
        return bsaName;
    }

    public void setBsaName(String bsaName) {
        this.bsaName = bsaName;
    }

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

    public String getCollegeShortName() {
        return collegeShortName;
    }

    public void setCollegeShortName(String collegeShortName) {
        this.collegeShortName = collegeShortName;
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

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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

    public List<Long> getSelectedCollegeIds() {
        return selectedCollegeIds;
    }

    public void setSelectedCollegeIds(List<Long> selectedCollegeIds) {
        this.selectedCollegeIds = selectedCollegeIds;
    }

    public List<CollegeInfo> getAvailableColleges() {
        return availableColleges;
    }

    public void setAvailableColleges(List<CollegeInfo> availableColleges) {
        this.availableColleges = availableColleges;
    }

    @Override
    public String toString() {
        return "BSACollegeRegistrationDTO{" +
                "registrationId=" + registrationId +
                ", bsaName='" + bsaName + '\'' +
                ", collegeName='" + collegeName + '\'' +
                ", registrationDate=" + registrationDate +
                '}';
    }
}
