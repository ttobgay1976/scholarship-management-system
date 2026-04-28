package com.sprms.system.core.servicesdao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sprms.system.hbmbeans.EmailAudit;

@Repository
public interface EmailAuditRepository extends JpaRepository<EmailAudit, Long> {

    // Find email history for a specific entity
    @Query("SELECT e FROM EmailAudit e WHERE e.relatedEntityType = :entityType AND e.relatedEntityId = :entityId ORDER BY e.createdDate DESC")
    List<EmailAudit> findEmailHistoryForEntity(@Param("entityType") String entityType, @Param("entityId") Long entityId);

    // Count emails by status
    long countByStatus(EmailAudit.EmailStatus status);

    // Find emails by multiple criteria
    @Query("SELECT e FROM EmailAudit e WHERE " +
           "(:recipientEmail IS NULL OR e.toEmail LIKE %:recipientEmail%) AND " +
           "(:emailType IS NULL OR e.emailType = :emailType) AND " +
           "(:status IS NULL OR e.status = :status) AND " +
           "(:startDate IS NULL OR e.createdDate >= :startDate) AND " +
           "(:endDate IS NULL OR e.createdDate <= :endDate) " +
           "ORDER BY e.createdDate DESC")
    List<EmailAudit> findByMultipleCriteria(
            @Param("recipientEmail") String recipientEmail,
            @Param("emailType") String emailType,
            @Param("status") EmailAudit.EmailStatus status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    // Find emails by recipient email
    List<EmailAudit> findByToEmailContaining(String recipientEmail);

    // Find emails by email type
    List<EmailAudit> findByEmailType(String emailType);

    // Find emails by date range
    @Query("SELECT e FROM EmailAudit e WHERE e.createdDate BETWEEN :startDate AND :endDate ORDER BY e.createdDate DESC")
    List<EmailAudit> findByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // Find failed emails for retry
    @Query("SELECT e FROM EmailAudit e WHERE e.status = 'FAILED' AND e.errorMessage IS NOT NULL ORDER BY e.createdDate DESC")
    List<EmailAudit> findFailedEmails();

    // Find recent emails (last 24 hours)
    @Query("SELECT e FROM EmailAudit e WHERE e.createdDate >= :since ORDER BY e.createdDate DESC")
    List<EmailAudit> findRecentEmails(@Param("since") LocalDateTime since);
}
