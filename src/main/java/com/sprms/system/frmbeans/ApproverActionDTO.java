package com.sprms.system.frmbeans;

import com.sprms.system.applicationEnums.ApplicationStatus;

public class ApproverActionDTO {

    private Long applicationId;
    private ApplicationStatus approvalStatus;
    private String approvalRemarks;
    
	public Long getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(Long applicationId) {
		this.applicationId = applicationId;
	}
	public String getApprovalRemarks() {
		return approvalRemarks;
	}
	public void setApprovalRemarks(String approvalRemarks) {
		this.approvalRemarks = approvalRemarks;
	}
	public ApplicationStatus getApprovalStatus() {
		return approvalStatus;
	}
	public void setApprovalStatus(ApplicationStatus approvalStatus) {
		this.approvalStatus = approvalStatus;
	}
    
    
}
