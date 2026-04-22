package com.sprms.system.frmbeans;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sprms.system.applicationEnums.ApplicationStatus;

public class VerifierActionDTO {

    @JsonProperty("applicationId")
    private Long applicationId;
    
    @JsonProperty("verifierStatus")
    private ApplicationStatus verifierStatus;
    
    @JsonProperty("verifierRemarks")
    private String verifierRemarks;
    
    
	public Long getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(Long applicationId) {
		this.applicationId = applicationId;
	}
	
	public String getVerifierRemarks() {
		return verifierRemarks;
	}
	public void setVerifierRemarks(String verifierRemarks) {
		this.verifierRemarks = verifierRemarks;
	}
	public ApplicationStatus getVerifierStatus() {
		return verifierStatus;
	}
	public void setVerifierStatus(ApplicationStatus verifierStatus) {
		this.verifierStatus = verifierStatus;
	}
    
    
}
