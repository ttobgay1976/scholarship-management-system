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
import org.springframework.web.bind.annotation.RestController;

import com.sprms.system.core.services.BSAMembershipService;
import com.sprms.system.core.servicesdao.BSAMembershipRepository;
import com.sprms.system.frmbeans.ApproverActionDTO;
import com.sprms.system.frmbeans.BSAMembershipDTO;
import com.sprms.system.frmbeans.VerifierActionDTO;
import com.sprms.system.applicationEnums.ApplicationStatus;
import com.sprms.system.hbmbeans.BSAMembership;
import com.sprms.system.hbmbeans.FundingType;
import com.sprms.system.hbmbeans.MembershipStatus;
import com.sprms.system.hbmbeans.User;
import com.sprms.system.hbmbeans.ScholarshipRegistration;
import com.sprms.system.hbmbeans.BSACollegeRegistration;
import com.sprms.system.user.dao.UserRepository;
import com.sprms.system.core.servicesdao.ScholarshipRegistrationRepository;
import com.sprms.system.core.servicesdao.BSACollegeRegistrationRepository;

@RestController
@RequestMapping("/api/bsa-membership")
public class BSAMembershipController {

    private static final Logger logger = LoggerFactory.getLogger(BSAMembershipController.class);

    private final BSAMembershipService _bsaMembershipService;
    private final UserRepository _userRepository;
    private final BSAMembershipRepository _bsaMembershipRepository;
    private final ScholarshipRegistrationRepository _scholarshipRegistrationRepository;
    private final BSACollegeRegistrationRepository _bsaCollegeRegistrationRepository;

    public BSAMembershipController(BSAMembershipService bsaMembershipService, UserRepository userRepository, BSAMembershipRepository bsaMembershipRepository, ScholarshipRegistrationRepository scholarshipRegistrationRepository, BSACollegeRegistrationRepository bsaCollegeRegistrationRepository) {
        this._bsaMembershipService = bsaMembershipService;
        this._userRepository = userRepository;
        this._bsaMembershipRepository = bsaMembershipRepository;
        this._scholarshipRegistrationRepository = scholarshipRegistrationRepository;
        this._bsaCollegeRegistrationRepository = bsaCollegeRegistrationRepository;
    }

    // Get available BSAs for membership registration
    @GetMapping("/bsas")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<Map<String, Object>>> getAvailableBSAs() {
        try {
            // For now, return a mock list. This should be updated to fetch from BSA repository
            List<Map<String, Object>> bsas = new ArrayList<>();
            
            // Mock data - replace with actual BSA repository call
            Map<String, Object> bsa1 = new HashMap<>();
            bsa1.put("id", 1L);
            bsa1.put("bsaName", "Royal University of Bhutan Students Association");
            bsa1.put("bsaCode", "RUBSA");
            bsa1.put("description", "Association for students of Royal University of Bhutan");
            bsas.add(bsa1);
            
            Map<String, Object> bsa2 = new HashMap<>();
            bsa2.put("id", 2L);
            bsa2.put("bsaName", "College of Science and Technology Students Association");
            bsa2.put("bsaCode", "CSTSA");
            bsa2.put("description", "Association for CST students");
            bsas.add(bsa2);
            
            return ResponseEntity.ok(bsas);
        } catch (Exception e) {
            logger.error("Error loading BSAs: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Search scholarship student by CID
    @GetMapping("/scholarship/search-by-cid/{cid}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Map<String, Object>> searchScholarshipStudentByCid(@PathVariable String cid) {
        try {
            List<ScholarshipRegistration> students = _scholarshipRegistrationRepository.findByCitizenId(cid);
            
            if (students.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("success", false, "error", "Student not found with CID: " + cid));
            }
            
            ScholarshipRegistration student = students.get(0); // Get first match
            Map<String, Object> studentData = new HashMap<>();
            studentData.put("id", student.getId());
            studentData.put("citizenId", student.getCitizenId());
            studentData.put("firstName", student.getFirstName());
            studentData.put("middleName", student.getMiddleName());
            studentData.put("lastName", student.getLastName());
            studentData.put("contactNo", student.getContactNo());
            studentData.put("emailAddress", student.getEmailAddress());
            studentData.put("indexNumber", student.getIndexNumber());
            studentData.put("permanentAddress", student.getPermanentAddress());
            studentData.put("countryOfCompletion", student.getCountryOfCompletion());
            
            return ResponseEntity.ok(Map.of("success", true, "student", studentData));
        } catch (Exception e) {
            logger.error("Error searching scholarship student by CID: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "error", "Error searching for student"));
        }
    }

    // Get colleges for BSA
    @GetMapping("/bsa-colleges/{bsaId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Map<String, Object>> getCollegesForBsa(@PathVariable Long bsaId) {
        try {
            List<BSACollegeRegistration> colleges = _bsaCollegeRegistrationRepository.findByBsaBsaIdAndStatus(bsaId, com.sprms.system.hbmbeans.RegistrationStatus.ACTIVE);
            
            List<Map<String, Object>> collegeList = new ArrayList<>();
            for (BSACollegeRegistration collegeReg : colleges) {
                Map<String, Object> collegeData = new HashMap<>();
                collegeData.put("collegeId", collegeReg.getCollege().getId());
                collegeData.put("collegeName", collegeReg.getCollege().getCollegeName());
                collegeData.put("country", collegeReg.getCountry().getCountryName());
                collegeData.put("state", collegeReg.getState().getStateName());
                collegeData.put("city", collegeReg.getCity().getCityName());
                collegeList.add(collegeData);
            }
            
            return ResponseEntity.ok(Map.of("success", true, "colleges", collegeList));
        } catch (Exception e) {
            logger.error("Error getting colleges for BSA: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "error", "Error loading colleges"));
        }
    }

    
    // Create new membership request
    @PostMapping("/request")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<BSAMembershipDTO> createMembershipRequest(@RequestBody Map<String, Object> request) {
        try {
            logger.info("Received membership request: {}", request);
            
            Long bsaId = Long.valueOf(request.get("bsaId").toString());
            FundingType fundingType = FundingType.valueOf(request.get("fundingType").toString());
            
            logger.info("Processing request - BSA ID: {}, Funding Type: {}", bsaId, fundingType);
            
            // Get current logged-in student
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            User student = _userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            // Extract manual student data if provided
            Map<String, Object> studentData = new HashMap<>();
            studentData.put("studentCid", request.get("studentCid"));
            studentData.put("studentName", request.get("studentName"));
            studentData.put("contactNumber", request.get("contactNumber"));
            studentData.put("emailAddress", request.get("emailAddress"));
            studentData.put("program", request.get("program"));
            studentData.put("collegeName", request.get("collegeName"));
            studentData.put("address", request.get("address"));
            studentData.put("collegeId", request.get("collegeId"));

            logger.info("Student data extracted: {}", studentData);

            BSAMembershipDTO result = _bsaMembershipService.createMembershipRequest(bsaId, student.getId(), fundingType, studentData);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error creating membership request: {}", e.getMessage());
            logger.error("Request data: {}", request.toString());
            logger.error("Stack trace: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    
    // Get pending memberships for VP approval
    @GetMapping("/pending-vp-approval")
    // @PreAuthorize("hasAnyRole('VP', 'PRESIDENT')")
    public ResponseEntity<Page<BSAMembershipDTO>> getPendingMembershipsForVpApproval(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<BSAMembershipDTO> memberships = _bsaMembershipService.getPendingMembershipsForVpApproval(page, size);
            return ResponseEntity.ok(memberships);
        } catch (Exception e) {
            logger.error("Error getting pending memberships for VP approval: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get approved memberships for focal officer verification
    @GetMapping("/approved-for-verification")
    @PreAuthorize("hasRole('FOCAL_OFFICER')")
    public ResponseEntity<Page<BSAMembershipDTO>> getApprovedMembershipsForVerification(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<BSAMembershipDTO> memberships = _bsaMembershipService.getApprovedMembershipsForVerification(page, size);
            return ResponseEntity.ok(memberships);
        } catch (Exception e) {
            logger.error("Error getting approved memberships for verification: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // VP/President approval action
    @PostMapping("/vp-approval")
    // @PreAuthorize("hasAnyRole('VP', 'PRESIDENT')")
    public ResponseEntity<Map<String, String>> processVpApproval(@RequestBody ApproverActionDTO dto) {
        try {
            _bsaMembershipService.processVpApproval(dto);
            return ResponseEntity.ok(Map.of("message", "VP approval processed successfully"));
        } catch (Exception e) {
            logger.error("Error processing VP approval: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Focal Officer verification action
    @PostMapping("/focal-verification")
    @PreAuthorize("hasRole('FOCAL_OFFICER')")
    public ResponseEntity<Map<String, Object>> processFocalVerification(@RequestBody VerifierActionDTO dto) {
        try {
            // Debug logging to see what's being received
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
            
            logger.info("Processing focal verification for membership ID: {}", dto.getApplicationId());
            _bsaMembershipService.processFocalVerification(dto);
            return ResponseEntity.ok(Map.of("success", true, "message", "Focal verification processed successfully"));
        } catch (Exception e) {
            logger.error("Error processing focal verification: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Get membership by ID
    @GetMapping("/{id}")
    // @PreAuthorize("hasAnyRole('STUDENT', 'VP', 'PRESIDENT', 'FOCAL_OFFICER', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> getMembershipById(@PathVariable Long id) {
        try {
            BSAMembership membership = _bsaMembershipRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Membership not found"));
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("membership", convertToDetailedDTO(membership));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error getting membership by ID: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    // Get student's memberships
    @GetMapping("/student/{studentId}")
    // @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    public ResponseEntity<List<BSAMembershipDTO>> getStudentMemberships(@PathVariable Long studentId) {
        try {
            // Check if the requesting user is the student themselves or an admin
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            User currentUser = _userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (!currentUser.getId().equals(studentId) && !auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            List<BSAMembershipDTO> memberships = _bsaMembershipService.getStudentMemberships(studentId);
            return ResponseEntity.ok(memberships);
        } catch (Exception e) {
            logger.error("Error getting student memberships: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get all memberships (for admin)
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<BSAMembershipDTO>> getAllMemberships(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<BSAMembershipDTO> memberships = _bsaMembershipService.getAllMemberships(page, size);
            return ResponseEntity.ok(memberships);
        } catch (Exception e) {
            logger.error("Error getting all memberships: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get membership statistics
    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'VP', 'PRESIDENT', 'FOCAL_OFFICER')")
    public ResponseEntity<Map<String, Long>> getMembershipStatistics() {
        try {
            Map<String, Long> statistics = Map.of(
                "pending", _bsaMembershipService.getMembershipCountByStatus(MembershipStatus.PENDING),
                "approved", _bsaMembershipService.getMembershipCountByStatus(MembershipStatus.APPROVED),
                "rejected", _bsaMembershipService.getMembershipCountByStatus(MembershipStatus.REJECTED),
                "verified", _bsaMembershipService.getMembershipCountByStatus(MembershipStatus.VERIFIED)
            );
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            logger.error("Error getting membership statistics: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get current student's memberships
    @GetMapping("/my-memberships")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<BSAMembershipDTO>> getMyMemberships() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            User currentUser = _userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            List<BSAMembershipDTO> memberships = _bsaMembershipService.getStudentMemberships(currentUser.getId());
            return ResponseEntity.ok(memberships);
        } catch (Exception e) {
            logger.error("Error getting current user memberships: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Process VP approval/rejection
    @PostMapping("/approve")
    // @PreAuthorize("hasAnyRole('VP', 'PRESIDENT')") // Temporarily disabled for testing
    public ResponseEntity<Map<String, Object>> processApproval(@RequestBody Map<String, Object> request) {
        try {
            Long membershipId = Long.valueOf(request.get("membershipId").toString());
            String decision = request.get("decision").toString();
            String remarks = request.get("remarks").toString();
            String approvalDate = request.get("approvalDate").toString();
            
            // Create approval DTO
            ApproverActionDTO approvalDTO = new ApproverActionDTO();
            approvalDTO.setApplicationId(membershipId);
            
            // Convert decision string to ApplicationStatus
            ApplicationStatus status = "APPROVED".equals(decision) ? 
                ApplicationStatus.APPROVED : ApplicationStatus.REJECTED;
            approvalDTO.setApprovalStatus(status);
            approvalDTO.setApprovalRemarks(remarks);
            
            // Process the approval
            _bsaMembershipService.processVpApproval(approvalDTO);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Request " + decision.toLowerCase() + "d successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error processing approval: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    
    // Helper method to convert BSAMembership to detailed DTO for View/Approve pages
    private Map<String, Object> convertToDetailedDTO(BSAMembership membership) {
        Map<String, Object> dto = new HashMap<>();
        dto.put("membershipId", membership.getMembershipId());
        dto.put("studentId", membership.getStudent().getId());
        dto.put("studentName", membership.getStudentName());
        dto.put("studentCid", membership.getStudentCid());
        dto.put("studentEmail", membership.getStudentEmail());
        dto.put("collegeName", membership.getCollege_Name());
        dto.put("bsaId", membership.getBsa().getBsaId());
        dto.put("bsaName", membership.getBsa().getBsaName());
        dto.put("fundingType", membership.getFundingType());
        dto.put("membershipStatus", membership.getMembershipStatus());
        dto.put("membershipRequestDate", membership.getMembershipRequestDate());
        dto.put("requestedDate", membership.getRequestedDate());
        dto.put("createdBy", membership.getCreatedBy());
        dto.put("createdDate", membership.getCreatedDate());
        
        // Include additional fields that might be populated manually
        dto.put("contactNumber", membership.getStudent() != null ? membership.getStudent().getContactno() : "");
        dto.put("program", ""); // This would need to be added to the entity if needed
        dto.put("address", ""); // This would need to be added to the entity if needed
        
        return dto;
    }
}
