package com.sprms.system.api.controller;

import com.sprms.system.core.services.BSAMembershipService;
import com.sprms.system.core.services.BSAFundRequestService;
import com.sprms.system.core.servicesdao.EmailAuditRepository;
import com.sprms.system.frmbeans.BSAMembershipDTO;
import com.sprms.system.frmbeans.BSAFundRequestDTO;
import com.sprms.system.hbmbeans.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/status-tracking")
public class StatusTrackingController {

    private static final Logger logger = LoggerFactory.getLogger(StatusTrackingController.class);

    @Autowired
    private BSAMembershipService membershipService;

    @Autowired
    private BSAFundRequestService fundRequestService;

    @Autowired
    private EmailAuditRepository emailAuditRepository;

    // Get membership requests with filtering
    @GetMapping("/memberships")
    @PreAuthorize("hasAnyRole('ADMIN', 'VP', 'President', 'Focal Officer')")
    public ResponseEntity<Map<String, Object>> getMembershipRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) MembershipStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) Long userId) {
        
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<BSAMembershipDTO> memberships;
            
            if (status != null || startDate != null || endDate != null || userId != null) {
                // Apply filtering logic here (would need to implement filtered queries in service)
                memberships = membershipService.getAllMemberships(page, size);
            } else {
                memberships = membershipService.getAllMemberships(page, size);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("memberships", memberships.getContent());
            response.put("currentPage", memberships.getNumber());
            response.put("totalItems", memberships.getTotalElements());
            response.put("totalPages", memberships.getTotalPages());
            Map<String, Object> filters = new HashMap<>();
            filters.put("status", status);
            filters.put("startDate", startDate);
            filters.put("endDate", endDate);
            filters.put("userId", userId);
            response.put("filters", filters);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error fetching membership requests: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // Get fund requests with filtering
    @GetMapping("/fund-requests")
    @PreAuthorize("hasAnyRole('ADMIN', 'VP', 'President', 'Focal Officer', 'Chief', 'Accountant')")
    public ResponseEntity<Map<String, Object>> getFundRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) FundRequestStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) Long userId) {
        
        try {
            // This would need to be implemented in the service
            Page<BSAFundRequestDTO> fundRequests = fundRequestService.getAllFundRequests(page, size);

            Map<String, Object> response = new HashMap<>();
            response.put("fundRequests", fundRequests.getContent());
            response.put("currentPage", fundRequests.getNumber());
            response.put("totalItems", fundRequests.getTotalElements());
            response.put("totalPages", fundRequests.getTotalPages());
            Map<String, Object> filters = new HashMap<>();
            filters.put("status", status);
            filters.put("startDate", startDate);
            filters.put("endDate", endDate);
            filters.put("userId", userId);
            response.put("filters", filters);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error fetching fund requests: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // Get membership request details with history
    @GetMapping("/memberships/{membershipId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VP', 'President', 'Focal Officer')")
    public ResponseEntity<Map<String, Object>> getMembershipDetails(@PathVariable Long membershipId) {
        try {
            BSAMembershipDTO membership = membershipService.getMembershipById(membershipId);
            List<EmailAudit> emailHistory = emailAuditRepository.findEmailHistoryForEntity("BSAMembership", membershipId);

            Map<String, Object> response = new HashMap<>();
            response.put("membership", membership);
            response.put("emailHistory", emailHistory);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error fetching membership details: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // Get fund request details with history
    @GetMapping("/fund-requests/{fundRequestId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VP', 'President', 'Focal Officer', 'Chief', 'Accountant')")
    public ResponseEntity<Map<String, Object>> getFundRequestDetails(@PathVariable Long fundRequestId) {
        try {
            BSAFundRequestDTO fundRequest = fundRequestService.getFundRequestById(fundRequestId);
            List<EmailAudit> emailHistory = emailAuditRepository.findEmailHistoryForEntity("BSAFundRequest", fundRequestId);

            Map<String, Object> response = new HashMap<>();
            response.put("fundRequest", fundRequest);
            response.put("emailHistory", emailHistory);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error fetching fund request details: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // Get dashboard statistics
    @GetMapping("/dashboard/stats")
    @PreAuthorize("hasAnyRole('ADMIN', 'VP', 'President', 'Focal Officer', 'Chief', 'Accountant')")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        try {
            Map<String, Object> stats = new HashMap<>();
            
            // Membership statistics
            stats.put("pendingMemberships", membershipService.getMembershipCountByStatus(MembershipStatus.PENDING));
            stats.put("approvedMemberships", membershipService.getMembershipCountByStatus(MembershipStatus.APPROVED));
            stats.put("verifiedMemberships", membershipService.getMembershipCountByStatus(MembershipStatus.VERIFIED));
            stats.put("rejectedMemberships", membershipService.getMembershipCountByStatus(MembershipStatus.REJECTED));
            
            // Fund request statistics (would need to implement these methods)
            stats.put("pendingFundRequests", 0L);
            stats.put("approvedFundRequests", 0L);
            stats.put("rejectedFundRequests", 0L);
            
            // Email statistics
            stats.put("sentEmails", emailAuditRepository.countByStatus(EmailAudit.EmailStatus.SENT));
            stats.put("failedEmails", emailAuditRepository.countByStatus(EmailAudit.EmailStatus.FAILED));

            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            logger.error("Error fetching dashboard statistics: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // Get email audit with filtering
    @GetMapping("/emails")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getEmailAudit(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String recipientEmail,
            @RequestParam(required = false) String emailType,
            @RequestParam(required = false) EmailAudit.EmailStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        try {
            List<EmailAudit> emails = emailAuditRepository.findByMultipleCriteria(
                recipientEmail, emailType, status, startDate, endDate);

            Map<String, Object> response = new HashMap<>();
            response.put("emails", emails);
            response.put("totalItems", emails.size());
            Map<String, Object> filters = new HashMap<>();
            filters.put("recipientEmail", recipientEmail);
            filters.put("emailType", emailType);
            filters.put("status", status);
            filters.put("startDate", startDate);
            filters.put("endDate", endDate);
            response.put("filters", filters);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error fetching email audit: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
