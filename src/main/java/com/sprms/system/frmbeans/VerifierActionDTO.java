package com.sprms.system.frmbeans;

import com.sprms.system.applicationEnums.ApplicationStatus;

public class VerifierActionDTO {

    private Long applicationId;
    private ApplicationStatus verifierStatus;
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
