package com.sprms.system.core.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.sprms.system.core.servicesdao.BSAMembershipRepository;
import com.sprms.system.core.servicesdao.StudentRepository;
import com.sprms.system.frmbeans.BSAMembershipDTO;
import com.sprms.system.frmbeans.ApproverActionDTO;
import com.sprms.system.frmbeans.VerifierActionDTO;
import com.sprms.system.hbmbeans.BSAMembership;
import com.sprms.system.hbmbeans.BSA;
import com.sprms.system.hbmbeans.FundingType;
import com.sprms.system.hbmbeans.MembershipStatus;
import com.sprms.system.hbmbeans.Student;
import com.sprms.system.hbmbeans.User;
import com.sprms.system.user.dao.UserRepository;
import com.sprms.system.master.dao.BSARegistrationRepository;

@Service
public class BSAMembershipService {

    private static final Logger logger = LoggerFactory.getLogger(BSAMembershipService.class);

    private final BSAMembershipRepository _bsaMembershipRepository;
    private final UserRepository _userRepository;
    private final BSARegistrationRepository _bsaRegistrationRepository;
    private final StudentRepository _studentRepository;

    public BSAMembershipService(BSAMembershipRepository bsaMembershipRepository, UserRepository userRepository, BSARegistrationRepository bsaRegistrationRepository, StudentRepository studentRepository) {
        this._bsaMembershipRepository = bsaMembershipRepository;
        this._userRepository = userRepository;
        this._bsaRegistrationRepository = bsaRegistrationRepository;
        this._studentRepository = studentRepository;
    }

    // Create new membership request
    public BSAMembershipDTO createMembershipRequest(Long bsaId, Long studentId, FundingType fundingType) {
        logger.info("@@@Creating BSA membership request for BSA ID: {}, Student ID: {}", bsaId, studentId);

        // Get current user (student)
        User student = _userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Get student entity to access college information
        Student studentEntity = _studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student details not found"));

        // Check if student already has a membership with this BSA
        List<BSAMembership> existingMemberships = _bsaMembershipRepository.findByStudent(student);
        for (BSAMembership existing : existingMemberships) {
            if (existing.getBsa().getBsaId().equals(bsaId) && 
                (existing.getMembershipStatus() == MembershipStatus.PENDING || 
                 existing.getMembershipStatus() == MembershipStatus.APPROVED || 
                 existing.getMembershipStatus() == MembershipStatus.VERIFIED)) {
                throw new RuntimeException("Student already has an active or pending membership with this BSA");
            }
        }

        // Create new membership request
        BSAMembership membership = new BSAMembership();
        membership.setStudent(student);
        membership.setStudentCid(student.getCidno());
        membership.setStudentEmail(student.getUsername());
        membership.setStudentName(student.getFirstname() + " " + student.getLastname());
        membership.setCollege_Name(studentEntity.getCollegeName());
        membership.setFundingType(fundingType);
        membership.setRequestedDate(LocalDateTime.now());
        membership.setMembershipRequestDate(LocalDateTime.now());
        membership.setMembershipStatus(MembershipStatus.PENDING);
        membership.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());

        // Fetch BSA from database
        BSA bsa = _bsaRegistrationRepository.findById(bsaId)
                .orElseThrow(() -> new RuntimeException("BSA not found with ID: " + bsaId));
        membership.setBsa(bsa);

        BSAMembership saved = _bsaMembershipRepository.save(membership);
        return convertToDTO(saved);
    }

    // Create new membership request with manual student data (for students without CID)
    public BSAMembershipDTO createMembershipRequest(Long bsaId, Long studentId, FundingType fundingType, Map<String, Object> studentData) {
        logger.info("@@@Creating BSA membership request for BSA ID: {}, Student ID: {} with manual data", bsaId, studentId);

        // Get current user (student)
        User student = _userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Check if student already has a membership with this BSA
        List<BSAMembership> existingMemberships = _bsaMembershipRepository.findByStudent(student);
        for (BSAMembership existing : existingMemberships) {
            if (existing.getBsa().getBsaId().equals(bsaId) && 
                (existing.getMembershipStatus() == MembershipStatus.PENDING || 
                 existing.getMembershipStatus() == MembershipStatus.APPROVED || 
                 existing.getMembershipStatus() == MembershipStatus.VERIFIED)) {
                throw new RuntimeException("Student already has an active or pending membership with this BSA");
            }
        }

        // Create new membership request with manual data
        BSAMembership membership = new BSAMembership();
        membership.setStudent(student);
        
        // Use manual data if provided, otherwise fall back to user data
        String studentCid = (String) studentData.get("studentCid");
        membership.setStudentCid(studentCid != null && !studentCid.isEmpty() ? studentCid : student.getCidno());
        
        String studentName = (String) studentData.get("studentName");
        membership.setStudentName(studentName != null && !studentName.isEmpty() ? studentName : student.getFirstname() + " " + student.getLastname());
        
        String emailAddress = (String) studentData.get("emailAddress");
        membership.setStudentEmail(emailAddress != null && !emailAddress.isEmpty() ? emailAddress : student.getUsername());
        
        String collegeName = (String) studentData.get("collegeName");
        membership.setCollege_Name(collegeName != null && !collegeName.isEmpty() ? collegeName : "");
        
        membership.setFundingType(fundingType);
        membership.setRequestedDate(LocalDateTime.now());
        membership.setMembershipRequestDate(LocalDateTime.now());
        membership.setMembershipStatus(MembershipStatus.PENDING);
        membership.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());

        // Fetch BSA from database
        BSA bsa = _bsaRegistrationRepository.findById(bsaId)
                .orElseThrow(() -> new RuntimeException("BSA not found with ID: " + bsaId));
        membership.setBsa(bsa);

        BSAMembership saved = _bsaMembershipRepository.save(membership);
        return convertToDTO(saved);
    }

    // Get pending memberships for VP approval
    public Page<BSAMembershipDTO> getPendingMembershipsForVpApproval(int page, int size) {
        logger.info("@@@Getting pending memberships for VP approval");
        
        Pageable pageable = PageRequest.of(page, size);
        Page<BSAMembership> memberships = _bsaMembershipRepository
                .findByMembershipStatusOrderByRequestDate(MembershipStatus.PENDING, pageable);
        
        return memberships.map(this::convertToDTO);
    }

    // Get approved memberships for focal officer verification
    public Page<BSAMembershipDTO> getApprovedMembershipsForVerification(int page, int size) {
        logger.info("@@@Getting approved memberships for focal officer verification");
        
        Pageable pageable = PageRequest.of(page, size);
        Page<BSAMembership> memberships = _bsaMembershipRepository
                .findByMembershipStatusOrderByVpApprovalDate(MembershipStatus.APPROVED, pageable);
        
        return memberships.map(this::convertToDTO);
    }

    // VP/President approval action
    public void processVpApproval(ApproverActionDTO dto) {
        logger.info("@@@Processing VP approval for membership ID: {}", dto.getApplicationId());

        BSAMembership membership = _bsaMembershipRepository.findById(dto.getApplicationId())
                .orElseThrow(() -> new RuntimeException("Membership not found"));

        if (membership.getMembershipStatus() != MembershipStatus.PENDING) {
            throw new RuntimeException("Membership is not in pending status");
        }

        // Get current user (VP/President)
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User vpUser = _userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("VP/President user not found"));

        membership.setMembershipStatus(dto.getApprovalStatus() == com.sprms.system.applicationEnums.ApplicationStatus.APPROVED ? 
                MembershipStatus.APPROVED : MembershipStatus.REJECTED);
        membership.setVpApprovalDate(LocalDateTime.now());
        membership.setVpApprovedBy(vpUser);
        membership.setVpRemarks(dto.getApprovalRemarks());
        membership.setUpdatedBy(username);
        membership.setUpdatedDate(LocalDateTime.now());

        _bsaMembershipRepository.save(membership);
    }

    // Focal Officer verification action
    public void processFocalVerification(VerifierActionDTO dto) {
        logger.info("@@@Processing focal verification for membership ID: {}", dto.getApplicationId());

        // Validate application ID
        if (dto.getApplicationId() == null) {
            throw new RuntimeException("Application ID cannot be null");
        }

        BSAMembership membership = _bsaMembershipRepository.findById(dto.getApplicationId())
                .orElseThrow(() -> new RuntimeException("Membership not found"));

        if (membership.getMembershipStatus() != MembershipStatus.APPROVED) {
            throw new RuntimeException("Membership is not approved by VP/President");
        }

        // Get current user (Focal Officer)
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User focalUser = _userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Focal Officer user not found"));

        membership.setMembershipStatus(dto.getVerifierStatus() == com.sprms.system.applicationEnums.ApplicationStatus.APPROVED ? 
                MembershipStatus.VERIFIED : dto.getVerifierStatus() == com.sprms.system.applicationEnums.ApplicationStatus.VERIFIED ? 
                MembershipStatus.VERIFIED : MembershipStatus.REJECTED);
        membership.setFocalVerificationDate(LocalDateTime.now());
        membership.setFocalVerifiedBy(focalUser);
        membership.setFocalRemarks(dto.getVerifierRemarks());
        membership.setUpdatedBy(username);
        membership.setUpdatedDate(LocalDateTime.now());

        // Additional logging to track membership state before save
        logger.info("About to save membership: ID={}, Status={}, FocalVerifiedBy={}, FocalRemarks={}", 
                membership.getMembershipId(), membership.getMembershipStatus(), 
                membership.getFocalVerifiedBy(), membership.getFocalRemarks());
        
        logger.info("Focal verification process: InputStatus={}, FinalStatus={}", 
                dto.getVerifierStatus(), membership.getMembershipStatus());

        // Final null check before save
        if (membership.getMembershipId() == null) {
            logger.error("Cannot save membership: membershipId is null");
            throw new RuntimeException("Membership ID cannot be null");
        }

        _bsaMembershipRepository.save(membership);
    }

    // Get membership by ID
    public BSAMembershipDTO getMembershipById(Long id) {
        BSAMembership membership = _bsaMembershipRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new RuntimeException("Membership not found"));
        return convertToDTO(membership);
    }

    // Get student's memberships
    public List<BSAMembershipDTO> getStudentMemberships(Long studentId) {
        List<BSAMembership> memberships = _bsaMembershipRepository.findByStudentIdWithBsaDetails(studentId);
        return memberships.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get all BSA memberships
    public Page<BSAMembershipDTO> getAllMemberships(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BSAMembership> memberships = _bsaMembershipRepository.findAll(pageable);
        return memberships.map(this::convertToDTO);
    }

    // Get membership statistics
    public long getMembershipCountByStatus(MembershipStatus status) {
        return _bsaMembershipRepository.countByMembershipStatus(status);
    }

    // Convert entity to DTO
    private BSAMembershipDTO convertToDTO(BSAMembership membership) {
        BSAMembershipDTO dto = new BSAMembershipDTO();
        dto.setMembershipId(membership.getMembershipId());
        dto.setBsaId(membership.getBsa().getBsaId());
        dto.setBsaName(membership.getBsa().getBsaName());
        dto.setBsaCode(membership.getBsa().getBsaCode());
        dto.setStudentId(membership.getStudent().getId());
        dto.setStudentName(membership.getStudent().getFirstname() + " " + membership.getStudent().getLastname());
        dto.setStudentCid(membership.getStudent().getCidno());
        dto.setFundingType(membership.getFundingType());
        dto.setMembershipStatus(membership.getMembershipStatus());
        dto.setMembershipRequestDate(membership.getMembershipRequestDate());
        dto.setVpApprovalDate(membership.getVpApprovalDate());
        dto.setVpApprovedBy(membership.getVpApprovedBy() != null ? 
                membership.getVpApprovedBy().getFirstname() + " " + membership.getVpApprovedBy().getLastname() : null);
        dto.setVpRemarks(membership.getVpRemarks());
        dto.setFocalVerificationDate(membership.getFocalVerificationDate());
        dto.setFocalVerifiedBy(membership.getFocalVerifiedBy() != null ? 
                membership.getFocalVerifiedBy().getFirstname() + " " + membership.getFocalVerifiedBy().getLastname() : null);
        dto.setFocalRemarks(membership.getFocalRemarks());
        dto.setCreatedDate(membership.getCreatedDate());
        return dto;
    }
}
