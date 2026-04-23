package com.sprms.system.frmbeans;

import java.time.LocalDateTime;

public class BSADTO {

    private Long bsaId;
    private String bsaCode;
    private String bsaName;
    private String description;

    private Long countryId;
    private String countryName;

    private Long stateId;
    private String stateName;

    private Long cityId;
    private String cityName;

    private Long instituteId;
    private String instituteName;

    private String status;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private String createdBy;
    private String updatedBy;

    // Constructors
    public BSADTO() {
    }

    public BSADTO(Long bsaId, String bsaCode, String bsaName, String status) {
        this.bsaId = bsaId;
        this.bsaCode = bsaCode;
        this.bsaName = bsaName;
        this.status = status;
    }

    // Getters and Setters
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Long getInstituteId() {
        return instituteId;
    }

    public void setInstituteId(Long instituteId) {
        this.instituteId = instituteId;
    }

    public String getInstituteName() {
        return instituteName;
    }

    public void setInstituteName(String instituteName) {
        this.instituteName = instituteName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
        return "BSADTO{" +
                "bsaId=" + bsaId +
                ", bsaCode='" + bsaCode + '\'' +
                ", bsaName='" + bsaName + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
