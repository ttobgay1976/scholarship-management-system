package com.sprms.system.hbmbeans;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "email_audit")
public class EmailAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "to_email", nullable = false)
    private String toEmail;

    @Column(name = "recipient_name")
    private String recipientName;

    @Column(name = "subject", nullable = false)
    private String subject;

    @Column(name = "email_body", columnDefinition = "TEXT")
    private String emailBody;

    @Column(name = "email_type", nullable = false)
    private String emailType;

    @Column(name = "related_entity_type")
    private String relatedEntityType;

    @Column(name = "related_entity_id")
    private Long relatedEntityId;

    @Column(name = "sent_by")
    private String sentBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EmailStatus status;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "sent_date")
    private LocalDateTime sentDate;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    public enum EmailStatus {
        PENDING,
        SENT,
        FAILED
    }

    // Default constructor
    public EmailAudit() {
        this.createdDate = LocalDateTime.now();
        this.status = EmailStatus.PENDING;
    }

    // Constructor with required fields
    public EmailAudit(String toEmail, String recipientName, String subject, String emailBody, 
                    String emailType, String relatedEntityType, Long relatedEntityId, String sentBy) {
        this();
        this.toEmail = toEmail;
        this.recipientName = recipientName;
        this.subject = subject;
        this.emailBody = emailBody;
        this.emailType = emailType;
        this.relatedEntityType = relatedEntityType;
        this.relatedEntityId = relatedEntityId;
        this.sentBy = sentBy;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getEmailBody() {
        return emailBody;
    }

    public void setEmailBody(String emailBody) {
        this.emailBody = emailBody;
    }

    public String getEmailType() {
        return emailType;
    }

    public void setEmailType(String emailType) {
        this.emailType = emailType;
    }

    public String getRelatedEntityType() {
        return relatedEntityType;
    }

    public void setRelatedEntityType(String relatedEntityType) {
        this.relatedEntityType = relatedEntityType;
    }

    public Long getRelatedEntityId() {
        return relatedEntityId;
    }

    public void setRelatedEntityId(Long relatedEntityId) {
        this.relatedEntityId = relatedEntityId;
    }

    public String getSentBy() {
        return sentBy;
    }

    public void setSentBy(String sentBy) {
        this.sentBy = sentBy;
    }

    public EmailStatus getStatus() {
        return status;
    }

    public void setStatus(EmailStatus status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public LocalDateTime getSentDate() {
        return sentDate;
    }

    public void setSentDate(LocalDateTime sentDate) {
        this.sentDate = sentDate;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    // Utility methods
    public void markAsSent() {
        this.status = EmailStatus.SENT;
        this.sentDate = LocalDateTime.now();
        this.errorMessage = null;
    }

    public void markAsFailed(String errorMessage) {
        this.status = EmailStatus.FAILED;
        this.errorMessage = errorMessage;
        this.sentDate = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "EmailAudit{" +
                "id=" + id +
                ", toEmail='" + toEmail + '\'' +
                ", subject='" + subject + '\'' +
                ", emailType='" + emailType + '\'' +
                ", status=" + status +
                ", createdDate=" + createdDate +
                '}';
    }
}
