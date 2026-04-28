package com.sprms.system.api.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sprms.system.core.services.VPPresidentMembershipService;
import com.sprms.system.core.servicesdao.VPPresidentMembershipRepository;
import com.sprms.system.frmbeans.VPPresidentMembershipDTO;
import com.sprms.system.frmbeans.VerifierActionDTO;
import com.sprms.system.hbmbeans.Position;
import com.sprms.system.hbmbeans.VPMembershipStatus;
import com.sprms.system.hbmbeans.User;
import com.sprms.system.user.dao.UserRepository;
import com.sprms.system.master.dao.BSARegistrationRepository;

@RestController
@RequestMapping("/api/vp-president-membership")
public class VPPresidentMembershipController {

    private static final Logger logger = LoggerFactory.getLogger(VPPresidentMembershipController.class);

    private final VPPresidentMembershipService vpPresidentMembershipService;
    private final UserRepository userRepository;
    private final VPPresidentMembershipRepository vpPresidentMembershipRepository;
    private final BSARegistrationRepository bsaRegistrationRepository;

    public VPPresidentMembershipController(VPPresidentMembershipService vpPresidentMembershipService, 
                                         UserRepository userRepository, 
                                         VPPresidentMembershipRepository vpPresidentMembershipRepository,
                                         BSARegistrationRepository bsaRegistrationRepository) {
        this.vpPresidentMembershipService = vpPresidentMembershipService;
        this.userRepository = userRepository;
        this.vpPresidentMembershipRepository = vpPresidentMembershipRepository;
        this.bsaRegistrationRepository = bsaRegistrationRepository;
    }

    // Get available BSAs for VP/President membership requests
    @GetMapping("/bsas")
    @PreAuthorize("hasAnyRole('PRESIDENT', 'VICE_PRESIDENT')")
    public ResponseEntity<List<Map<String, Object>>> getAvailableBSAs() {
        try {
            // For now, return a mock list. This should be updated to fetch from BSA repository
            List<Map<String, Object>> bsas = List.of(
                Map.of("id", 1L, "bsaName", "Royal University of Bhutan Students Association", "bsaCode", "RUBSA", 
                       "description", "Association for students of Royal University of Bhutan"),
                Map.of("id", 2L, "bsaName", "College of Science and Technology Students Association", "bsaCode", "CSTSA", 
                       "description", "Association for CST students"),
                Map.of("id", 3L, "bsaName", "Sherubtse College Students Association", "bsaCode", "SCSA", 
                       "description", "Association for Sherubtse College students")
            );
            
            return ResponseEntity.ok(bsas);
        } catch (Exception e) {
            logger.error("Error loading BSAs: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get available positions
    @GetMapping("/positions")
    @PreAuthorize("hasAnyRole('PRESIDENT', 'VICE_PRESIDENT')")
    public ResponseEntity<List<Map<String, Object>>> getAvailablePositions() {
        try {
            List<Map<String, Object>> positions = List.of(
                Map.of("value", "PRESIDENT", "displayName", "President"),
                Map.of("value", "VICE_PRESIDENT", "displayName", "Vice President"),
                Map.of("value", "GENERAL_SECRETARY", "displayName", "General Secretary"),
                Map.of("value", "TREASURER", "displayName", "Treasurer"),
                Map.of("value", "EXECUTIVE_MEMBER", "displayName", "Executive Member"),
                Map.of("value", "ADVISOR", "displayName", "Advisor")
            );
            
            return ResponseEntity.ok(positions);
        } catch (Exception e) {
            logger.error("Error loading positions: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Create new VP/President membership request (public access)
    @PostMapping("/public-request")
    public ResponseEntity<VPPresidentMembershipDTO> createPublicMembershipRequest(
            @RequestParam("requesterName") String requesterName,
            @RequestParam("email") String email,
            @RequestParam("contactNumber") String contactNumber,
            @RequestParam("cidNo") String cidNo,
            @RequestParam("bsaId") Long bsaId,
            @RequestParam("requestedPosition") String requestedPositionStr,
            @RequestParam("requestReason") String requestReason,
            @RequestParam(value = "experienceDocument", required = false) MultipartFile experienceDocument) {
        try {
            logger.info("Received PUBLIC VP/President membership request:");
            logger.info("  - Requester Name: {}", requesterName);
            logger.info("  - Email: {}", email);
            logger.info("  - Contact Number: {}", contactNumber);
            logger.info("  - CID No: {}", cidNo);
            logger.info("  - BSA ID: {}", bsaId);
            logger.info("  - Requested Position: {}", requestedPositionStr);
            logger.info("  - Request Reason: {}", requestReason);
            
            Position requestedPosition = Position.valueOf(requestedPositionStr);
            
            // Handle experience document upload if provided
            String documentPath = null;
            if (experienceDocument != null && !experienceDocument.isEmpty()) {
                logger.info("Processing experience document: {}, Size: {} bytes", 
                           experienceDocument.getOriginalFilename(), experienceDocument.getSize());
                
                // Generate unique filename
                String timestamp = String.valueOf(System.currentTimeMillis());
                String originalFilename = experienceDocument.getOriginalFilename();
                String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                String uniqueFilename = "bsa_fund_request_" + timestamp + fileExtension;
                
                // Create upload directory if it doesn't exist
                String uploadDir = "uploads/bsa-fund-request/";
                java.io.File directory = new java.io.File(uploadDir);
                if (!directory.exists()) {
                    directory.mkdirs();
                }
                
                // Save file to filesystem
                String filePath = uploadDir + uniqueFilename;
                try {
                    experienceDocument.transferTo(new java.io.File(filePath));
                    documentPath = filePath;
                    logger.info("Document saved successfully to: {}", documentPath);
                } catch (Exception e) {
                    logger.error("Failed to save document: {}", e.getMessage());
                    // Continue without document - don't fail the entire request
                }
            }

            VPPresidentMembershipDTO result = vpPresidentMembershipService.createPublicMembershipRequest(
                requesterName, email, contactNumber, bsaId, requestedPosition, requestReason, "", documentPath);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error creating VP/President membership request: {}", e.getMessage());
            logger.error("Exception type: {}", e.getClass().getSimpleName());
            logger.error("Stack trace: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Get pending requests for focal officer verification
    @GetMapping("/pending-verification")
    @PreAuthorize("hasRole('FOCAL_OFFICER')")
    public ResponseEntity<Page<VPPresidentMembershipDTO>> getPendingRequestsForVerification(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<VPPresidentMembershipDTO> requests = vpPresidentMembershipService.getPendingRequestsForVerification(page, size);
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            logger.error("Error getting pending requests for verification: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Focal Officer verification action
    @PostMapping("/focal-verification")
    @PreAuthorize("hasRole('FOCAL_OFFICER')")
    public ResponseEntity<Map<String, Object>> processFocalVerification(@RequestBody VerifierActionDTO dto) {
        try {
            logger.info("Received focal verification request: applicationId={}, status={}, remarks={}", 
                dto.getApplicationId(), dto.getVerifierStatus(), dto.getVerifierRemarks());
            
            // Validate that applicationId is not null
            if (dto.getApplicationId() == null) {
                logger.error("Application ID is null in received DTO");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Membership ID cannot be null"));
            }
            
            // Validate that verifierStatus is not null
            if (dto.getVerifierStatus() == null) {
                logger.error("Verifier status is null in received DTO");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Verification decision cannot be null"));
            }
            
            logger.info("Processing focal verification for VP/President membership ID: {}", dto.getApplicationId());
            vpPresidentMembershipService.processFocalVerification(dto);
            return ResponseEntity.ok(Map.of("success", true, "message", "Focal verification processed successfully"));
        } catch (Exception e) {
            logger.error("Error processing focal verification: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Get membership request by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('PRESIDENT', 'VICE_PRESIDENT', 'FOCAL_OFFICER', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> getMembershipById(@PathVariable Long id) {
        try {
            VPPresidentMembershipDTO membership = vpPresidentMembershipService.getMembershipById(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("membership", membership);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error getting membership by ID: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    // Get requester's membership requests
    @GetMapping("/my-requests")
    @PreAuthorize("hasAnyRole('PRESIDENT', 'VICE_PRESIDENT')")
    public ResponseEntity<List<VPPresidentMembershipDTO>> getMyRequests() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            User currentUser = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            List<VPPresidentMembershipDTO> requests = vpPresidentMembershipService.getRequesterMemberships(currentUser.getId());
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            logger.error("Error getting current user requests: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    
    // Get membership statistics
    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'FOCAL_OFFICER')")
    public ResponseEntity<Map<String, Long>> getMembershipStatistics() {
        try {
            Map<String, Long> statistics = Map.of(
                "pending", vpPresidentMembershipService.getMembershipCountByStatus(VPMembershipStatus.PENDING),
                "approved", vpPresidentMembershipService.getMembershipCountByStatus(VPMembershipStatus.APPROVED),
                "rejected", vpPresidentMembershipService.getMembershipCountByStatus(VPMembershipStatus.REJECTED)
            );
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            logger.error("Error getting membership statistics: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get all membership requests (for list page)
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'FOCAL_OFFICER', 'PRESIDENT', 'VICE_PRESIDENT')")
    public ResponseEntity<Page<VPPresidentMembershipDTO>> getAllMembershipRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<VPPresidentMembershipDTO> requests = vpPresidentMembershipService.getAllMembershipRequests(page, size);
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            logger.error("Error getting all membership requests: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get membership requests by status
    @GetMapping("/by-status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FOCAL_OFFICER', 'PRESIDENT', 'VICE_PRESIDENT')")
    public ResponseEntity<Page<VPPresidentMembershipDTO>> getMembershipRequestsByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            VPMembershipStatus membershipStatus = VPMembershipStatus.valueOf(status.toUpperCase());
            Page<VPPresidentMembershipDTO> requests = vpPresidentMembershipService.getMembershipRequestsByStatus(membershipStatus, page, size);
            return ResponseEntity.ok(requests);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid status parameter: {}", status);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            logger.error("Error getting membership requests by status: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
