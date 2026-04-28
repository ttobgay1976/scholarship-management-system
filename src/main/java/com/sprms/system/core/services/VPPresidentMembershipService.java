package com.sprms.system.core.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.sprms.system.core.servicesdao.VPPresidentMembershipRepository;
import com.sprms.system.user.dao.UserRepository;
import com.sprms.system.frmbeans.VPPresidentMembershipDTO;
import com.sprms.system.frmbeans.VerifierActionDTO;
import com.sprms.system.hbmbeans.BSA;
import com.sprms.system.hbmbeans.Position;
import com.sprms.system.hbmbeans.VPMembershipStatus;
import com.sprms.system.hbmbeans.VPPresidentMembership;
import com.sprms.system.hbmbeans.User;
import com.sprms.system.master.dao.BSARegistrationRepository;
import com.sprms.system.applicationEnums.ApplicationStatus;

@Service
public class VPPresidentMembershipService {

    private static final Logger logger = LoggerFactory.getLogger(VPPresidentMembershipService.class);

    private final VPPresidentMembershipRepository vpPresidentMembershipRepository;
    private final UserRepository userRepository;
    private final BSARegistrationRepository bsaRegistrationRepository;
    private final EmailService emailService;

    public VPPresidentMembershipService(VPPresidentMembershipRepository vpPresidentMembershipRepository, 
                                      UserRepository userRepository, 
                                      BSARegistrationRepository bsaRegistrationRepository,
                                      EmailService emailService) {
        this.vpPresidentMembershipRepository = vpPresidentMembershipRepository;
        this.userRepository = userRepository;
        this.bsaRegistrationRepository = bsaRegistrationRepository;
        this.emailService = emailService;
    }

    // Create new VP/President membership request (public access - no authentication required)
    public VPPresidentMembershipDTO createPublicMembershipRequest(String requesterName, String email, String contactNumber, 
                                                                 Long bsaId, Position requestedPosition, String requestReason, String experienceDetails, String documentPath) {
        logger.info("@@@Creating PUBLIC VP/President membership request for BSA ID: {}, Requested Position: {}", 
                   bsaId, requestedPosition);

        // For public requests, find or create a system user to satisfy database constraint
        User requester = findOrCreateSystemUser();

        // For public requests, skip duplicate checking since there's no authenticated user
        // TODO: Could implement duplicate checking based on email or CID if needed

        // Fetch BSA from database
        BSA bsa = bsaRegistrationRepository.findById(bsaId)
                .orElseThrow(() -> new RuntimeException("BSA not found with ID: " + bsaId));

        // Create new membership request
        VPPresidentMembership membership = new VPPresidentMembership(requester, requestedPosition, bsa, requestReason);
        
        // Debug logging
        logger.info("System user CID: {}", requester.getCidno());
        logger.info("Membership CID fields after constructor - requesterCid: {}, requesterCidDuplicate: {}", 
                   membership.getRequesterCid(), membership.getRequesterCidDuplicate());
        
        // Override dummy user data with actual form data
        membership.setRequesterName(requesterName); // Set custom requester name
        membership.setRequesterEmail(email); // Set custom email
        membership.setContactNumber(contactNumber); // Set contact number
        membership.setExperienceDetails(experienceDetails);
        membership.setDocumentPath(documentPath); // Set document path
        membership.setUpdatedBy("Public Submission"); // For public requests
        membership.setUpdatedDate(LocalDateTime.now());
        
        // Ensure CID fields are not null
        if (membership.getRequesterCid() == null) {
            logger.warn("requesterCid is null, setting fallback value");
            membership.setRequesterCid("PUBLIC_SYSTEM_" + System.currentTimeMillis());
        }
        if (membership.getRequesterCidDuplicate() == null) {
            logger.warn("requesterCidDuplicate is null, setting fallback value");
            membership.setRequesterCidDuplicate(membership.getRequesterCid());
        }

        VPPresidentMembership saved = vpPresidentMembershipRepository.save(membership);
        logger.info("VP/President membership request created with ID: {}", saved.getVpPresidentMembershipId());
        logger.info("Saved membership details:");
        logger.info("  - Requester Name: {}", saved.getRequesterName());
        logger.info("  - Email: {}", saved.getRequesterEmail());
        logger.info("  - Contact Number: {}", saved.getContactNumber());
        logger.info("  - Document Path: {}", saved.getDocumentPath());
        logger.info("  - BSA ID: {}", saved.getBsa().getBsaId());
        logger.info("  - Requested Position: {}", saved.getRequestedPosition());
        
        // Verify the entity was actually saved by retrieving it from database
        VPPresidentMembership verifySaved = vpPresidentMembershipRepository.findById(saved.getVpPresidentMembershipId()).orElse(null);
        if (verifySaved != null) {
            logger.info("VERIFICATION: Entity successfully retrieved from database");
            logger.info("  - Retrieved Contact Number: {}", verifySaved.getContactNumber());
            logger.info("  - Retrieved Document Path: {}", verifySaved.getDocumentPath());
        } else {
            logger.error("VERIFICATION FAILED: Entity not found in database after save!");
        }

        // Send email notification to requester about request receipt
        try {
            emailService.sendMembershipReceivedEmail(
                email,
                requesterName,
                saved.getVpPresidentMembershipId()
            );
            logger.info("Request received confirmation email sent to: {}", email);
        } catch (Exception e) {
            logger.error("Failed to send request received email for membership ID: {}", 
                        saved.getVpPresidentMembershipId(), e);
            // Continue without failing the request creation process
        }
        
        return convertToDTO(saved);
    }

    // Find or create system user for public requests to satisfy database constraint
    private User findOrCreateSystemUser() {
        // Try to find an existing system user first
        Optional<User> systemUser = userRepository.findByUsername("system@public.user");
        if (systemUser.isPresent()) {
            logger.info("Found existing system user for public requests");
            return systemUser.get();
        }
        
        // Create a new system user if not found
        User newUser = new User();
        newUser.setId(System.currentTimeMillis()); // Assign unique ID manually
        newUser.setFirstname("Public");
        newUser.setLastname("Submission");
        newUser.setUsername("system@public.user");
        newUser.setCidno("PUBLIC_SYSTEM_" + System.currentTimeMillis());
        newUser.setCreatedat(LocalDateTime.now());
        newUser.setUpdateat(LocalDateTime.now());
        newUser.setStatus(1); // Active status
        
        // Save the user to database first to avoid transient entity error
        User savedUser = userRepository.save(newUser);
        logger.info("Created new system user for public requests with ID: {}", savedUser.getId());
        
        return savedUser;
    }

    // Get pending requests for focal officer verification
    public Page<VPPresidentMembershipDTO> getPendingRequestsForVerification(int page, int size) {
        logger.info("@@@Getting pending VP/President membership requests for focal officer verification");
        
        Pageable pageable = PageRequest.of(page, size);
        Page<VPPresidentMembership> memberships = vpPresidentMembershipRepository
                .findByMembershipStatusForVerification(VPMembershipStatus.PENDING, pageable);
        
        return memberships.map(this::convertToDTO);
    }

    // Focal Officer verification action
    public void processFocalVerification(VerifierActionDTO dto) {
        logger.info("@@@Processing focal verification for VP/President membership ID: {}", dto.getApplicationId());

        // Validate application ID
        if (dto.getApplicationId() == null) {
            throw new RuntimeException("Application ID cannot be null");
        }

        VPPresidentMembership membership = vpPresidentMembershipRepository.findById(dto.getApplicationId())
                .orElseThrow(() -> new RuntimeException("VP/President membership request not found"));

        if (membership.getMembershipStatus() != VPMembershipStatus.PENDING) {
            throw new RuntimeException("Membership request is not in pending status");
        }

        // Get current user (Focal Officer)
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User focalUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Focal Officer user not found"));

        // Update membership status
        membership.setMembershipStatus(dto.getVerifierStatus() == ApplicationStatus.APPROVED ? 
                VPMembershipStatus.APPROVED : VPMembershipStatus.REJECTED);
        membership.setFocalVerificationDate(LocalDateTime.now());
        membership.setFocalVerifiedBy(focalUser);
        membership.setFocalRemarks(dto.getVerifierRemarks());
        membership.setUpdatedBy(username);
        membership.setUpdatedDate(LocalDateTime.now());

        // Save the updated membership
        VPPresidentMembership savedMembership = vpPresidentMembershipRepository.save(membership);
        logger.info("VP/President membership {} verification completed: {} by {}", 
                savedMembership.getVpPresidentMembershipId(), savedMembership.getMembershipStatus(), username);

        // Send email notification to the requester
        try {
            if (savedMembership.getMembershipStatus() == VPMembershipStatus.APPROVED) {
                emailService.sendMembershipApprovalEmail(
                    savedMembership.getRequesterEmail(),
                    savedMembership.getRequesterName(),
                    savedMembership.getVpPresidentMembershipId()
                );
                logger.info("Approval email sent to requester: {}", savedMembership.getRequesterEmail());
            } else if (savedMembership.getMembershipStatus() == VPMembershipStatus.REJECTED) {
                emailService.sendMembershipRejectionEmail(
                    savedMembership.getRequesterEmail(),
                    savedMembership.getRequesterName(),
                    savedMembership.getVpPresidentMembershipId(),
                    savedMembership.getFocalRemarks()
                );
                logger.info("Rejection email sent to requester: {}", savedMembership.getRequesterEmail());
            }
        } catch (Exception e) {
            logger.error("Failed to send email notification for membership ID: {}", 
                        savedMembership.getVpPresidentMembershipId(), e);
            // Continue without failing the verification process
        }
    }

    // Get membership request by ID
    public VPPresidentMembershipDTO getMembershipById(Long id) {
        VPPresidentMembership membership = vpPresidentMembershipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("VP/President membership request not found"));
        return convertToDTO(membership);
    }

    // Get requester's membership requests
    public List<VPPresidentMembershipDTO> getRequesterMemberships(Long requesterId) {
        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new RuntimeException("Requester not found"));
        
        List<VPPresidentMembership> memberships = vpPresidentMembershipRepository.findByRequester(requester);
        return memberships.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get all membership requests (for admin)
    public Page<VPPresidentMembershipDTO> getAllMembershipRequests(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<VPPresidentMembership> memberships = vpPresidentMembershipRepository.findAll(pageable);
        return memberships.map(this::convertToDTO);
    }

    // Get membership requests by status
    public Page<VPPresidentMembershipDTO> getMembershipRequestsByStatus(VPMembershipStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<VPPresidentMembership> memberships = vpPresidentMembershipRepository.findByMembershipStatusOrderByRequestDate(status, pageable);
        return memberships.map(this::convertToDTO);
    }

    // Get membership statistics
    public long getMembershipCountByStatus(VPMembershipStatus status) {
        return vpPresidentMembershipRepository.countByMembershipStatus(status);
    }

    // Helper method to validate requester position
    private boolean isValidRequesterPosition(Position position) {
        return position == Position.PRESIDENT || position == Position.VICE_PRESIDENT;
    }

    // Convert entity to DTO
    private VPPresidentMembershipDTO convertToDTO(VPPresidentMembership membership) {
        VPPresidentMembershipDTO dto = new VPPresidentMembershipDTO();
        dto.setVpPresidentMembershipId(membership.getVpPresidentMembershipId());
        dto.setRequesterId(membership.getRequester().getId());
        dto.setRequesterName(membership.getRequesterName());
        dto.setRequesterCid(membership.getRequesterCid());
        dto.setRequesterEmail(membership.getRequesterEmail());
        dto.setContactNumber(membership.getContactNumber());
        dto.setRequestedPosition(membership.getRequestedPosition());
        dto.setBsaId(membership.getBsa().getBsaId());
        dto.setBsaName(membership.getBsa().getBsaName());
        dto.setBsaCode(membership.getBsa().getBsaCode());
        dto.setRequestReason(membership.getRequestReason());
        dto.setExperienceDetails(membership.getExperienceDetails());
        dto.setDocumentPath(membership.getDocumentPath());
        dto.setRequestDate(membership.getRequestDate());
        dto.setMembershipStatus(membership.getMembershipStatus());
        dto.setFocalVerificationDate(membership.getFocalVerificationDate());
        dto.setFocalVerifiedBy(membership.getFocalVerifiedBy() != null ? 
                membership.getFocalVerifiedBy().getFirstname() + " " + membership.getFocalVerifiedBy().getLastname() : null);
        dto.setFocalRemarks(membership.getFocalRemarks());
        dto.setCreatedDate(membership.getCreatedDate());
        dto.setCreatedBy(membership.getCreatedBy());
        return dto;
    }
}
