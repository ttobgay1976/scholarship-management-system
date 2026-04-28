package com.sprms.system.api.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sprms.system.core.services.BSAFundRequestService;
import com.sprms.system.master.dao.BSARegistrationRepository;
import com.sprms.system.user.dao.UserRepository;
import com.sprms.system.frmbeans.BSAFundRequestDTO;
import com.sprms.system.frmbeans.BSAFundRequestFormBean;
import com.sprms.system.frmbeans.FundRequestVerificationDTO;
import com.sprms.system.hbmbeans.BSA;
import com.sprms.system.hbmbeans.User;

@RestController
@RequestMapping("/api/bsa-fund-request")
public class BSAFundRequestController {

    private static final Logger logger = LoggerFactory.getLogger(BSAFundRequestController.class);

    private final BSAFundRequestService bsaFundRequestService;
    private final UserRepository userRepository;
    private final BSARegistrationRepository bsaRepository;

    public BSAFundRequestController(BSAFundRequestService bsaFundRequestService, 
                                   UserRepository userRepository, 
                                   BSARegistrationRepository bsaRepository) {
        this.bsaFundRequestService = bsaFundRequestService;
        this.userRepository = userRepository;
        this.bsaRepository = bsaRepository;
    }

    // Get available BSAs for fund request submission
    @GetMapping("/available-bsas")
    @PreAuthorize("hasAnyRole('PRESIDENT', 'VP')")
    public ResponseEntity<List<Map<String, Object>>> getAvailableBSAs() {
        try {
            List<BSA> bsas = bsaRepository.findAllActiveBSAs();
            List<Map<String, Object>> result = new ArrayList<>();
            
            for (BSA bsa : bsas) {
                Map<String, Object> bsaMap = new HashMap<>();
                bsaMap.put("id", bsa.getBsaId());
                bsaMap.put("bsaName", bsa.getBsaName());
                bsaMap.put("bsaCode", bsa.getBsaCode());
                bsaMap.put("description", bsa.getDescription());
                result.add(bsaMap);
            }
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error loading available BSAs: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get participant count for BSA
    @GetMapping("/participant-count/{bsaId}")
    @PreAuthorize("hasAnyRole('PRESIDENT', 'VP')")
    public ResponseEntity<Map<String, Object>> getParticipantCount(@PathVariable Long bsaId) {
        try {
            logger.info("API request: Get participant count for BSA ID: {}", bsaId);
            long participantCount = bsaFundRequestService.getParticipantCountByBsa(bsaId);
            Map<String, Object> response = new HashMap<>();
            response.put("participantCount", participantCount);
            logger.info("API response: Returning participant count {} for BSA ID: {}", participantCount, bsaId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error getting participant count for BSA {}: {}", bsaId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Temporary test endpoint for debugging
    @GetMapping("/test-membership-data")
    public ResponseEntity<Map<String, Object>> testMembershipData() {
        try {
            Map<String, Object> response = new HashMap<>();
            
            // Test total verified members
            long totalVerified = bsaFundRequestService.getParticipantCountByBsa(1L);
            response.put("totalVerifiedForBsa1", totalVerified);
            
            // Test with different BSA IDs
            long bsa2Count = bsaFundRequestService.getParticipantCountByBsa(2L);
            response.put("totalVerifiedForBsa2", bsa2Count);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error in test endpoint: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // FR-BSA-06: Submit fund request
    @PostMapping(value = "/submit", consumes = "multipart/form-data")
    @PreAuthorize("hasAnyRole('PRESIDENT', 'VP')")
    public ResponseEntity<Map<String, Object>> submitFundRequest(
            @RequestPart("requestData") Map<String, Object> request,
            @RequestPart(value = "proposalDocumentFile", required = false) MultipartFile proposalDocumentFile,
            @RequestPart(value = "participantListFile", required = false) MultipartFile participantListFile,
            @RequestPart(value = "bsaRegistrationFile", required = false) MultipartFile bsaRegistrationFile,
            @RequestPart(value = "supportingDocumentFile", required = false) MultipartFile supportingDocumentFile) {
        try {
            logger.info("Received fund request submission");

            // Get current logged-in user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            User currentUser = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Extract and validate required fields
            Long bsaId = Long.valueOf(request.get("bsaId").toString());
            String activityDescription = request.get("activityDescription").toString();
            String jointAccountDetails = request.get("jointAccountDetails").toString();
            String bankDetails = request.get("bankDetails").toString();
            Double requestedAmount = Double.valueOf(request.get("requestedAmount").toString());

            // Validate mandatory fields
            if (activityDescription == null || activityDescription.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", "Activity description is required"
                ));
            }

            if (jointAccountDetails == null || jointAccountDetails.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", "Joint account details are required"
                ));
            }

            if (bankDetails == null || bankDetails.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", "Bank details are required"
                ));
            }

            // Create form bean
            BSAFundRequestFormBean formBean = new BSAFundRequestFormBean();
            formBean.setBsaId(bsaId);
            
            // Log file reception
            logger.info("Received proposal document: {}", proposalDocumentFile != null ? proposalDocumentFile.getOriginalFilename() : "null");
            logger.info("Received participant list file: {}", participantListFile != null ? participantListFile.getOriginalFilename() : "null");
            logger.info("Received BSA registration file: {}", bsaRegistrationFile != null ? bsaRegistrationFile.getOriginalFilename() : "null");
            logger.info("Received supporting document: {}", supportingDocumentFile != null ? supportingDocumentFile.getOriginalFilename() : "null");
            
            // Set files from @RequestPart parameters
            formBean.setProposalDocumentFile(proposalDocumentFile);
            formBean.setParticipantListFile(participantListFile);
            formBean.setBsaRegistrationFile(bsaRegistrationFile);
            formBean.setSupportingDocumentFile(supportingDocumentFile);
            
            formBean.setActivityDescription(activityDescription);
            formBean.setJointAccountDetails(jointAccountDetails);
            formBean.setBankDetails(bankDetails);
            formBean.setRequestedAmount(java.math.BigDecimal.valueOf(requestedAmount));

            // Handle participants if provided
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> participantsData = (List<Map<String, Object>>) request.get("participants");
            if (participantsData != null && !participantsData.isEmpty()) {
                List<BSAFundRequestFormBean.ParticipantFormBean> participants = new ArrayList<>();
                for (Map<String, Object> participantData : participantsData) {
                    BSAFundRequestFormBean.ParticipantFormBean participant = new BSAFundRequestFormBean.ParticipantFormBean();
                    participant.setParticipantName(participantData.get("participantName") != null ? participantData.get("participantName").toString() : null);
                    participant.setParticipantCid(participantData.get("participantCid") != null ? participantData.get("participantCid").toString() : null);
                    participant.setContactNumber(participantData.get("contactNumber") != null ? participantData.get("contactNumber").toString() : null);
                    participant.setEmailAddress(participantData.get("emailAddress") != null ? participantData.get("emailAddress").toString() : null);
                    participant.setRoleDesignation(participantData.get("roleDesignation") != null ? participantData.get("roleDesignation").toString() : null);
                    participant.setCollegeDepartment(participantData.get("collegeDepartment") != null ? participantData.get("collegeDepartment").toString() : null);
                    participants.add(participant);
                }
                formBean.setParticipants(participants);
            }

            // Submit fund request
            BSAFundRequestDTO result = bsaFundRequestService.submitFundRequest(formBean, currentUser.getId());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Fund request submitted successfully");
            response.put("fundRequest", result);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error submitting fund request: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "success", false,
                "error", "Failed to submit fund request: " + e.getMessage()
            ));
        }
    }

    // FR-BSA-07: Get pending fund requests for focal officer verification
    @GetMapping("/pending-verification")
    @PreAuthorize("hasRole('FOCAL_OFFICER')")
    public ResponseEntity<Page<BSAFundRequestDTO>> getPendingFundRequestsForVerification(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<BSAFundRequestDTO> fundRequests = bsaFundRequestService.getPendingFundRequestsForVerification(
                    org.springframework.data.domain.PageRequest.of(page, size));
            return ResponseEntity.ok(fundRequests);
        } catch (Exception e) {
            logger.error("Error getting pending fund requests for verification: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // FR-BSA-07: Process focal officer verification
    @PostMapping("/focal-verification")
    @PreAuthorize("hasRole('FOCAL_OFFICER')")
    public ResponseEntity<Map<String, Object>> processFocalVerification(@RequestBody FundRequestVerificationDTO verificationDTO) {
        try {
            logger.info("Processing focal verification for fund request ID: {}", verificationDTO.getFundRequestId());

            // Get current logged-in user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            User currentUser = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Validate verification data
            if (verificationDTO.getFundRequestId() == null) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", "Fund request ID is required"
                ));
            }

            if (verificationDTO.getVerifierStatus() == null || verificationDTO.getVerifierStatus().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", "Verification decision is required"
                ));
            }

            // If verified, approved amount is required
            if ("VERIFIED".equals(verificationDTO.getVerifierStatus()) && verificationDTO.getApprovedAmount() == null) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", "Approved amount is required when verifying a fund request"
                ));
            }

            BSAFundRequestDTO result = bsaFundRequestService.processFocalVerification(verificationDTO, currentUser.getId());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Focal verification processed successfully");
            response.put("fundRequest", result);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error processing focal verification: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "success", false,
                "error", "Failed to process focal verification: " + e.getMessage()
            ));
        }
    }

    // FR-BSA-08: Get verified fund requests for chief approval
    @GetMapping("/verified-for-chief-approval")
    @PreAuthorize("hasRole('CHIEF')")
    public ResponseEntity<Page<BSAFundRequestDTO>> getVerifiedFundRequestsForChiefApproval(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<BSAFundRequestDTO> fundRequests = bsaFundRequestService.getVerifiedFundRequestsForChiefApproval(
                    org.springframework.data.domain.PageRequest.of(page, size));
            return ResponseEntity.ok(fundRequests);
        } catch (Exception e) {
            logger.error("Error getting verified fund requests for chief approval: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // FR-BSA-08: Process chief approval
    @PostMapping("/chief-approval")
    @PreAuthorize("hasRole('CHIEF')")
    public ResponseEntity<Map<String, Object>> processChiefApproval(@RequestBody FundRequestVerificationDTO approvalDTO) {
        try {
            logger.info("Processing chief approval for fund request ID: {}", approvalDTO.getFundRequestId());

            // Get current logged-in user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            User currentUser = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Validate approval data
            if (approvalDTO.getFundRequestId() == null) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", "Fund request ID is required"
                ));
            }

            if (approvalDTO.getVerifierStatus() == null || approvalDTO.getVerifierStatus().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", "Approval decision is required"
                ));
            }

            BSAFundRequestDTO result = bsaFundRequestService.processChiefApproval(approvalDTO, currentUser.getId());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Chief approval processed successfully");
            response.put("fundRequest", result);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error processing chief approval: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "success", false,
                "error", "Failed to process chief approval: " + e.getMessage()
            ));
        }
    }

    // FR-BSA-08: Get chief approved fund requests for accountant approval
    @GetMapping("/chief-approved-for-accountant")
    @PreAuthorize("hasRole('ACCOUNTANT')")
    public ResponseEntity<Page<BSAFundRequestDTO>> getChiefApprovedFundRequestsForAccountant(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<BSAFundRequestDTO> fundRequests = bsaFundRequestService.getChiefApprovedFundRequestsForAccountant(
                    org.springframework.data.domain.PageRequest.of(page, size));
            return ResponseEntity.ok(fundRequests);
        } catch (Exception e) {
            logger.error("Error getting chief approved fund requests for accountant: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // FR-BSA-08: Process accountant approval
    @PostMapping("/accountant-approval")
    @PreAuthorize("hasRole('ACCOUNTANT')")
    public ResponseEntity<Map<String, Object>> processAccountantApproval(@RequestBody FundRequestVerificationDTO approvalDTO) {
        try {
            logger.info("Processing accountant approval for fund request ID: {}", approvalDTO.getFundRequestId());

            // Get current logged-in user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            User currentUser = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Validate approval data
            if (approvalDTO.getFundRequestId() == null) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", "Fund request ID is required"
                ));
            }

            if (approvalDTO.getVerifierStatus() == null || approvalDTO.getVerifierStatus().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", "Approval decision is required"
                ));
            }

            BSAFundRequestDTO result = bsaFundRequestService.processAccountantApproval(approvalDTO, currentUser.getId());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Accountant approval processed successfully");
            response.put("fundRequest", result);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error processing accountant approval: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "success", false,
                "error", "Failed to process accountant approval: " + e.getMessage()
            ));
        }
    }

    // Get fund request by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('PRESIDENT', 'VP', 'FOCAL_OFFICER', 'CHIEF', 'ACCOUNTANT', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> getFundRequestById(@PathVariable Long id) {
        try {
            BSAFundRequestDTO fundRequest = bsaFundRequestService.getFundRequestById(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("fundRequest", fundRequest);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error getting fund request by ID: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    // Get current user's fund requests
    @GetMapping("/my-requests")
    @PreAuthorize("hasAnyRole('PRESIDENT', 'VP')")
    public ResponseEntity<Page<BSAFundRequestDTO>> getMyFundRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            User currentUser = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Page<BSAFundRequestDTO> fundRequests = bsaFundRequestService.getFundRequestsByUser(
                    currentUser.getId(), org.springframework.data.domain.PageRequest.of(page, size));
            
            return ResponseEntity.ok(fundRequests);
        } catch (Exception e) {
            logger.error("Error getting current user's fund requests: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get fund request statistics
    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'FOCAL_OFFICER', 'CHIEF', 'ACCOUNTANT')")
    public ResponseEntity<Map<String, Object>> getFundRequestStatistics() {
        try {
            List<Object[]> statistics = bsaFundRequestService.getFundRequestStatistics();
            
            Map<String, Long> statsMap = new HashMap<>();
            for (Object[] stat : statistics) {
                String status = stat[0].toString();
                Long count = Long.valueOf(stat[1].toString());
                statsMap.put(status.toLowerCase(), count);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("statistics", statsMap);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error getting fund request statistics: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
