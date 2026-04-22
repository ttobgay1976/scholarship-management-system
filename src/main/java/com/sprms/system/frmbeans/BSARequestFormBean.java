package com.sprms.system.frmbeans;

import org.springframework.web.multipart.MultipartFile;

public class BSARequestFormBean {
    
    private Long requestId;
    private String bsaName;
    private Long countryId;
    private Long stateId;
    private Long cityId;
    private String presidentCid;
    private String vicePresidentCid;
    private String description;
    private MultipartFile supportingDocumentFile;
    
    // Constructors
    public BSARequestFormBean() {}
    
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
    
    public MultipartFile getSupportingDocumentFile() {
        return supportingDocumentFile;
    }
    
    public void setSupportingDocumentFile(MultipartFile supportingDocumentFile) {
        this.supportingDocumentFile = supportingDocumentFile;
    }
}
