package com.sprms.system.frmbeans;

import java.math.BigDecimal;

public class FundRequestVerificationDTO {
    
    private Long fundRequestId;
    private String verifierStatus; // VERIFIED, REJECTED
    private String verifierRemarks;
    private BigDecimal approvedAmount;
    private String verificationDate;

    // Constructors
    public FundRequestVerificationDTO() {}

    public FundRequestVerificationDTO(Long fundRequestId, String verifierStatus, String verifierRemarks, BigDecimal approvedAmount) {
        this.fundRequestId = fundRequestId;
        this.verifierStatus = verifierStatus;
        this.verifierRemarks = verifierRemarks;
        this.approvedAmount = approvedAmount;
    }

    // Getters and Setters
    public Long getFundRequestId() {
        return fundRequestId;
    }

    public void setFundRequestId(Long fundRequestId) {
        this.fundRequestId = fundRequestId;
    }

    public String getVerifierStatus() {
        return verifierStatus;
    }

    public void setVerifierStatus(String verifierStatus) {
        this.verifierStatus = verifierStatus;
    }

    public String getVerifierRemarks() {
        return verifierRemarks;
    }

    public void setVerifierRemarks(String verifierRemarks) {
        this.verifierRemarks = verifierRemarks;
    }

    public BigDecimal getApprovedAmount() {
        return approvedAmount;
    }

    public void setApprovedAmount(BigDecimal approvedAmount) {
        this.approvedAmount = approvedAmount;
    }

    public String getVerificationDate() {
        return verificationDate;
    }

    public void setVerificationDate(String verificationDate) {
        this.verificationDate = verificationDate;
    }
}
