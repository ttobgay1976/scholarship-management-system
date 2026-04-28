package com.sprms.system.frmbeans;

import java.util.List;

public class BSACollegeRegistrationFormBean {

    private Long bsaId;
    private String bsaName;
    
    private Long countryId;
    private Long stateId;
    private Long cityId;
    
    private List<Long> selectedCollegeIds;
    private List<BSACollegeRegistrationDTO.CollegeInfo> availableColleges;
    
    private String remarks;
    private String action; // CREATE, UPDATE, DELETE
    
    // Validation flags
    private boolean showBsaError;
    private boolean showCountryError;
    private boolean showStateError;
    private boolean showCityError;
    private boolean showCollegeError;
    
    // Success/Error messages
    private String successMessage;
    private String errorMessage;

    // Constructors
    public BSACollegeRegistrationFormBean() {
        this.action = "CREATE";
    }

    // Getters and Setters
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

    public List<Long> getSelectedCollegeIds() {
        return selectedCollegeIds;
    }

    public void setSelectedCollegeIds(List<Long> selectedCollegeIds) {
        this.selectedCollegeIds = selectedCollegeIds;
    }

    public List<BSACollegeRegistrationDTO.CollegeInfo> getAvailableColleges() {
        return availableColleges;
    }

    public void setAvailableColleges(List<BSACollegeRegistrationDTO.CollegeInfo> availableColleges) {
        this.availableColleges = availableColleges;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public boolean isShowBsaError() {
        return showBsaError;
    }

    public void setShowBsaError(boolean showBsaError) {
        this.showBsaError = showBsaError;
    }

    public boolean isShowCountryError() {
        return showCountryError;
    }

    public void setShowCountryError(boolean showCountryError) {
        this.showCountryError = showCountryError;
    }

    public boolean isShowStateError() {
        return showStateError;
    }

    public void setShowStateError(boolean showStateError) {
        this.showStateError = showStateError;
    }

    public boolean isShowCityError() {
        return showCityError;
    }

    public void setShowCityError(boolean showCityError) {
        this.showCityError = showCityError;
    }

    public boolean isShowCollegeError() {
        return showCollegeError;
    }

    public void setShowCollegeError(boolean showCollegeError) {
        this.showCollegeError = showCollegeError;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "BSACollegeRegistrationFormBean{" +
                "bsaId=" + bsaId +
                ", bsaName='" + bsaName + '\'' +
                ", countryId=" + countryId +
                ", stateId=" + stateId +
                ", cityId=" + cityId +
                ", action='" + action + '\'' +
                '}';
    }
}
