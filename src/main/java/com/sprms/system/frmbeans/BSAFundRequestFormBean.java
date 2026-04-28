package com.sprms.system.frmbeans;

import org.springframework.web.multipart.MultipartFile;
import java.math.BigDecimal;
import java.util.List;
import com.sprms.system.hbmbeans.EventType;

public class BSAFundRequestFormBean {
    
    private Long fundRequestId;
    private Long bsaId;
    private String activityDescription;
    private EventType eventType;
    private String jointAccountDetails;
    private String bankDetails;
    private BigDecimal requestedAmount;
    
    // Document uploads
    private MultipartFile proposalDocumentFile;
    private MultipartFile participantListFile;
    private MultipartFile bsaRegistrationFile;
    private MultipartFile supportingDocumentFile;
    
    // Participant list
    private List<ParticipantFormBean> participants;
    
    // Validation and action fields
    private String action; // submit, save_draft
    private String comments;

    // Constructors
    public BSAFundRequestFormBean() {}

    // Getters and Setters
    public Long getFundRequestId() {
        return fundRequestId;
    }

    public void setFundRequestId(Long fundRequestId) {
        this.fundRequestId = fundRequestId;
    }

    public Long getBsaId() {
        return bsaId;
    }

    public void setBsaId(Long bsaId) {
        this.bsaId = bsaId;
    }

    public String getActivityDescription() {
        return activityDescription;
    }

    public void setActivityDescription(String activityDescription) {
        this.activityDescription = activityDescription;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public String getJointAccountDetails() {
        return jointAccountDetails;
    }

    public void setJointAccountDetails(String jointAccountDetails) {
        this.jointAccountDetails = jointAccountDetails;
    }

    public String getBankDetails() {
        return bankDetails;
    }

    public void setBankDetails(String bankDetails) {
        this.bankDetails = bankDetails;
    }

    public BigDecimal getRequestedAmount() {
        return requestedAmount;
    }

    public void setRequestedAmount(BigDecimal requestedAmount) {
        this.requestedAmount = requestedAmount;
    }

    public MultipartFile getProposalDocumentFile() {
        return proposalDocumentFile;
    }

    public void setProposalDocumentFile(MultipartFile proposalDocumentFile) {
        this.proposalDocumentFile = proposalDocumentFile;
    }

    public MultipartFile getParticipantListFile() {
        return participantListFile;
    }

    public void setParticipantListFile(MultipartFile participantListFile) {
        this.participantListFile = participantListFile;
    }

    public MultipartFile getBsaRegistrationFile() {
        return bsaRegistrationFile;
    }

    public void setBsaRegistrationFile(MultipartFile bsaRegistrationFile) {
        this.bsaRegistrationFile = bsaRegistrationFile;
    }

    public MultipartFile getSupportingDocumentFile() {
        return supportingDocumentFile;
    }

    public void setSupportingDocumentFile(MultipartFile supportingDocumentFile) {
        this.supportingDocumentFile = supportingDocumentFile;
    }

    public List<ParticipantFormBean> getParticipants() {
        return participants;
    }

    public void setParticipants(List<ParticipantFormBean> participants) {
        this.participants = participants;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    // Inner class for participant form data
    public static class ParticipantFormBean {
        private String participantName;
        private String participantCid;
        private String contactNumber;
        private String emailAddress;
        private String roleDesignation;
        private String collegeDepartment;

        // Constructors
        public ParticipantFormBean() {}

        // Getters and Setters
        public String getParticipantName() {
            return participantName;
        }

        public void setParticipantName(String participantName) {
            this.participantName = participantName;
        }

        public String getParticipantCid() {
            return participantCid;
        }

        public void setParticipantCid(String participantCid) {
            this.participantCid = participantCid;
        }

        public String getContactNumber() {
            return contactNumber;
        }

        public void setContactNumber(String contactNumber) {
            this.contactNumber = contactNumber;
        }

        public String getEmailAddress() {
            return emailAddress;
        }

        public void setEmailAddress(String emailAddress) {
            this.emailAddress = emailAddress;
        }

        public String getRoleDesignation() {
            return roleDesignation;
        }

        public void setRoleDesignation(String roleDesignation) {
            this.roleDesignation = roleDesignation;
        }

        public String getCollegeDepartment() {
            return collegeDepartment;
        }

        public void setCollegeDepartment(String collegeDepartment) {
            this.collegeDepartment = collegeDepartment;
        }
    }
}
