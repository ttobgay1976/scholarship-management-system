package com.sprms.system.core.services;

import com.sprms.system.core.servicesdao.EmailAuditRepository;
import com.sprms.system.hbmbeans.EmailAudit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailAuditRepository emailAuditRepository;

    private static final Map<String, String> EMAIL_TEMPLATES = new HashMap<>();

    static {
        EMAIL_TEMPLATES.put("MEMBERSHIP_SUBMISSION", 
            "Dear {recipientName},\n\n" +
            "A new VP/President membership request has been submitted.\n\n" +
            "Request Details:\n" +
            "- Membership ID: {membershipId}\n" +
            "- Applicant Name: {applicantName}\n" +
            "- Submission Date: {submissionDate}\n" +
            "- Funding Type: {fundingType}\n\n" +
            "Action Required: Please review and process this membership request.\n\n" +
            "Best regards,\n" +
            "Scholarship Management System Team");

        EMAIL_TEMPLATES.put("MEMBERSHIP_VP_APPROVAL", 
            "Dear {recipientName},\n\n" +
            "Your VP/President membership request has been APPROVED by VP/President.\n\n" +
            "Request Details:\n" +
            "- Membership ID: {membershipId}\n" +
            "- Approval Date: {approvalDate}\n" +
            "- Remarks: {remarks}\n\n" +
            "Your request will now be forwarded to the Focal Officer for verification.\n\n" +
            "Best regards,\n" +
            "Scholarship Management System Team");

        EMAIL_TEMPLATES.put("MEMBERSHIP_VP_REJECTION", 
            "Dear {recipientName},\n\n" +
            "Your VP/President membership request has been REJECTED by VP/President.\n\n" +
            "Request Details:\n" +
            "- Membership ID: {membershipId}\n" +
            "- Rejection Date: {rejectionDate}\n" +
            "- Remarks: {remarks}\n\n" +
            "If you have any questions, please contact the administration.\n\n" +
            "Best regards,\n" +
            "Scholarship Management System Team");

        EMAIL_TEMPLATES.put("MEMBERSHIP_FOCAL_VERIFICATION", 
            "Dear {recipientName},\n\n" +
            "Your VP/President membership request has been {verificationStatus} by the Focal Officer.\n\n" +
            "Request Details:\n" +
            "- Membership ID: {membershipId}\n" +
            "- Verification Date: {verificationDate}\n" +
            "- Remarks: {remarks}\n\n" +
            "{nextSteps}\n\n" +
            "Best regards,\n" +
            "Scholarship Management System Team");

        EMAIL_TEMPLATES.put("FUND_REQUEST_SUBMISSION", 
            "Dear {recipientName},\n\n" +
            "A new fund request has been submitted.\n\n" +
            "Request Details:\n" +
            "- Fund Request ID: {fundRequestId}\n" +
            "- Requested By: {requestedBy}\n" +
            "- Amount: {amount}\n" +
            "- Purpose: {purpose}\n" +
            "- Submission Date: {submissionDate}\n\n" +
            "Action Required: Please review and process this fund request.\n\n" +
            "Best regards,\n" +
            "Scholarship Management System Team");

        EMAIL_TEMPLATES.put("FUND_REQUEST_APPROVAL", 
            "Dear {recipientName},\n\n" +
            "Your fund request has been APPROVED.\n\n" +
            "Request Details:\n" +
            "- Fund Request ID: {fundRequestId}\n" +
            "- Approved Amount: {approvedAmount}\n" +
            "- Approval Date: {approvalDate}\n" +
            "- Remarks: {remarks}\n\n" +
            "Your fund request has been processed successfully.\n\n" +
            "Best regards,\n" +
            "Scholarship Management System Team");

        EMAIL_TEMPLATES.put("FUND_REQUEST_REJECTION", 
            "Dear {recipientName},\n\n" +
            "Your fund request has been REJECTED.\n\n" +
            "Request Details:\n" +
            "- Fund Request ID: {fundRequestId}\n" +
            "- Rejection Date: {rejectionDate}\n" +
            "- Remarks: {remarks}\n\n" +
            "If you have any questions, please contact the administration.\n\n" +
            "Best regards,\n" +
            "Scholarship Management System Team");
    }

    private String processTemplate(String templateKey, Map<String, String> variables) {
        String template = EMAIL_TEMPLATES.get(templateKey);
        if (template == null) {
            throw new IllegalArgumentException("Email template not found: " + templateKey);
        }

        String processedTemplate = template;
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            processedTemplate = processedTemplate.replace("{" + entry.getKey() + "}", 
                entry.getValue() != null ? entry.getValue() : "");
        }
        return processedTemplate;
    }

    @Transactional
    public boolean sendEmailWithAudit(String toEmail, String recipientName, String subject, 
                                    String emailBody, String emailType, String relatedEntityType, 
                                    Long relatedEntityId, String sentBy) {
        EmailAudit emailAudit = new EmailAudit(toEmail, recipientName, subject, emailBody, 
                                             emailType, relatedEntityType, relatedEntityId, sentBy);
        
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(emailBody);
            
            mailSender.send(message);
            
            emailAudit.markAsSent();
            emailAuditRepository.save(emailAudit);
            
            logger.info("Email sent successfully to: {} for {} ID: {}", toEmail, relatedEntityType, relatedEntityId);
            return true;
        } catch (Exception e) {
            emailAudit.markAsFailed(e.getMessage());
            emailAuditRepository.save(emailAudit);
            
            logger.error("Failed to send email to: {} for {} ID: {}", toEmail, relatedEntityType, relatedEntityId, e);
            return false;
        }
    }

    public void sendMembershipSubmissionEmail(String toEmail, String recipientName, Long membershipId, 
                                           String applicantName, String submissionDate, String fundingType) {
        Map<String, String> variables = new HashMap<>();
        variables.put("recipientName", recipientName);
        variables.put("membershipId", membershipId.toString());
        variables.put("applicantName", applicantName);
        variables.put("submissionDate", submissionDate);
        variables.put("fundingType", fundingType);

        String emailBody = processTemplate("MEMBERSHIP_SUBMISSION", variables);
        String subject = "New VP/President Membership Request Submitted";
        
        sendEmailWithAudit(toEmail, recipientName, subject, emailBody, "MEMBERSHIP_SUBMISSION", 
                          "BSAMembership", membershipId, "SYSTEM");
    }

    public void sendMembershipVPApprovalEmail(String toEmail, String recipientName, Long membershipId, 
                                            String approvalDate, String remarks) {
        Map<String, String> variables = new HashMap<>();
        variables.put("recipientName", recipientName);
        variables.put("membershipId", membershipId.toString());
        variables.put("approvalDate", approvalDate);
        variables.put("remarks", remarks != null ? remarks : "No remarks provided");

        String emailBody = processTemplate("MEMBERSHIP_VP_APPROVAL", variables);
        String subject = "VP/President Membership Request Approved by VP/President";
        
        sendEmailWithAudit(toEmail, recipientName, subject, emailBody, "MEMBERSHIP_VP_APPROVAL", 
                          "BSAMembership", membershipId, "SYSTEM");
    }

    public void sendMembershipVPRejectionEmail(String toEmail, String recipientName, Long membershipId, 
                                             String rejectionDate, String remarks) {
        Map<String, String> variables = new HashMap<>();
        variables.put("recipientName", recipientName);
        variables.put("membershipId", membershipId.toString());
        variables.put("rejectionDate", rejectionDate);
        variables.put("remarks", remarks != null ? remarks : "No remarks provided");

        String emailBody = processTemplate("MEMBERSHIP_VP_REJECTION", variables);
        String subject = "VP/President Membership Request Rejected by VP/President";
        
        sendEmailWithAudit(toEmail, recipientName, subject, emailBody, "MEMBERSHIP_VP_REJECTION", 
                          "BSAMembership", membershipId, "SYSTEM");
    }

    public void sendMembershipFocalVerificationEmail(String toEmail, String recipientName, Long membershipId, 
                                                     String verificationStatus, String verificationDate, 
                                                     String remarks, String nextSteps) {
        Map<String, String> variables = new HashMap<>();
        variables.put("recipientName", recipientName);
        variables.put("membershipId", membershipId.toString());
        variables.put("verificationStatus", verificationStatus);
        variables.put("verificationDate", verificationDate);
        variables.put("remarks", remarks != null ? remarks : "No remarks provided");
        variables.put("nextSteps", nextSteps);

        String emailBody = processTemplate("MEMBERSHIP_FOCAL_VERIFICATION", variables);
        String subject = "VP/President Membership Request " + verificationStatus + " by Focal Officer";
        
        sendEmailWithAudit(toEmail, recipientName, subject, emailBody, "MEMBERSHIP_FOCAL_VERIFICATION", 
                          "BSAMembership", membershipId, "SYSTEM");
    }

    public void sendFundRequestSubmissionEmail(String toEmail, String recipientName, Long fundRequestId, 
                                             String requestedBy, String amount, String purpose, 
                                             String submissionDate) {
        Map<String, String> variables = new HashMap<>();
        variables.put("recipientName", recipientName);
        variables.put("fundRequestId", fundRequestId.toString());
        variables.put("requestedBy", requestedBy);
        variables.put("amount", amount);
        variables.put("purpose", purpose);
        variables.put("submissionDate", submissionDate);

        String emailBody = processTemplate("FUND_REQUEST_SUBMISSION", variables);
        String subject = "New Fund Request Submitted";
        
        sendEmailWithAudit(toEmail, recipientName, subject, emailBody, "FUND_REQUEST_SUBMISSION", 
                          "BSAFundRequest", fundRequestId, "SYSTEM");
    }

    public void sendFundRequestApprovalEmail(String toEmail, String recipientName, Long fundRequestId, 
                                           String approvedAmount, String approvalDate, String remarks) {
        Map<String, String> variables = new HashMap<>();
        variables.put("recipientName", recipientName);
        variables.put("fundRequestId", fundRequestId.toString());
        variables.put("approvedAmount", approvedAmount);
        variables.put("approvalDate", approvalDate);
        variables.put("remarks", remarks != null ? remarks : "No remarks provided");

        String emailBody = processTemplate("FUND_REQUEST_APPROVAL", variables);
        String subject = "Fund Request Approved";
        
        sendEmailWithAudit(toEmail, recipientName, subject, emailBody, "FUND_REQUEST_APPROVAL", 
                          "BSAFundRequest", fundRequestId, "SYSTEM");
    }

    public void sendFundRequestRejectionEmail(String toEmail, String recipientName, Long fundRequestId, 
                                            String rejectionDate, String remarks) {
        Map<String, String> variables = new HashMap<>();
        variables.put("recipientName", recipientName);
        variables.put("fundRequestId", fundRequestId.toString());
        variables.put("rejectionDate", rejectionDate);
        variables.put("remarks", remarks != null ? remarks : "No remarks provided");

        String emailBody = processTemplate("FUND_REQUEST_REJECTION", variables);
        String subject = "Fund Request Rejected";
        
        sendEmailWithAudit(toEmail, recipientName, subject, emailBody, "FUND_REQUEST_REJECTION", 
                          "BSAFundRequest", fundRequestId, "SYSTEM");
    }

    public void sendMembershipApprovalEmail(String toEmail, String requesterName, Long membershipId) {
        Map<String, String> variables = new HashMap<>();
        variables.put("recipientName", requesterName);
        variables.put("membershipId", membershipId.toString());
        variables.put("approvalDate", java.time.LocalDateTime.now().toString());
        variables.put("remarks", "Approved by focal officer");

        String emailBody = processTemplate("MEMBERSHIP_FOCAL_VERIFICATION", variables);
        String subject = "VP/President Membership Request Approved";
        
        sendEmailWithAudit(toEmail, requesterName, subject, emailBody, "MEMBERSHIP_FOCAL_VERIFICATION", 
                          "BSAMembership", membershipId, "SYSTEM");
    }

    public void sendMembershipRejectionEmail(String toEmail, String requesterName, Long membershipId, String rejectionReason) {
        Map<String, String> variables = new HashMap<>();
        variables.put("recipientName", requesterName);
        variables.put("membershipId", membershipId.toString());
        variables.put("rejectionDate", java.time.LocalDateTime.now().toString());
        variables.put("remarks", rejectionReason != null && !rejectionReason.trim().isEmpty() ? rejectionReason : "No specific reason provided");

        String emailBody = processTemplate("MEMBERSHIP_FOCAL_VERIFICATION", variables);
        String subject = "VP/President Membership Request Rejected";
        
        sendEmailWithAudit(toEmail, requesterName, subject, emailBody, "MEMBERSHIP_FOCAL_VERIFICATION", 
                          "BSAMembership", membershipId, "SYSTEM");
    }

    public void sendMembershipReceivedEmail(String toEmail, String requesterName, Long membershipId) {
        Map<String, String> variables = new HashMap<>();
        variables.put("recipientName", requesterName);
        variables.put("membershipId", membershipId.toString());
        variables.put("applicantName", requesterName);
        variables.put("submissionDate", java.time.LocalDateTime.now().toString());
        variables.put("fundingType", "Not specified");

        String emailBody = processTemplate("MEMBERSHIP_SUBMISSION", variables);
        String subject = "VP/President Membership Request Received";
        
        sendEmailWithAudit(toEmail, requesterName, subject, emailBody, "MEMBERSHIP_SUBMISSION", 
                          "BSAMembership", membershipId, "SYSTEM");
    }
}
