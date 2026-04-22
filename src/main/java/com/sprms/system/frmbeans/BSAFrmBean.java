package com.sprms.system.frmbeans;

public class BSAFrmBean {

    private Long bsaId;
    private String bsaCode;
    private String bsaName;
    private String description;
    private Long countryId;
    private Long stateId;
    private Long cityId;
    private Long instituteId;
    private String status;

    // Constructors
    public BSAFrmBean() {
    }

    public BSAFrmBean(Long bsaId, String bsaCode, String bsaName) {
        this.bsaId = bsaId;
        this.bsaCode = bsaCode;
        this.bsaName = bsaName;
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

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public Long getInstituteId() {
        return instituteId;
    }

    public void setInstituteId(Long instituteId) {
        this.instituteId = instituteId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "BSAFrmBean{" +
                "bsaId=" + bsaId +
                ", bsaCode='" + bsaCode + '\'' +
                ", bsaName='" + bsaName + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
