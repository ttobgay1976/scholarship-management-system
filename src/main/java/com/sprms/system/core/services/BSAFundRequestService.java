package com.sprms.system.core.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sprms.system.core.servicesdao.BSAFundRequestRepository;
import com.sprms.system.core.servicesdao.BSAFundRequestDocumentRepository;
import com.sprms.system.core.servicesdao.BSAFundRequestParticipantRepository;
import com.sprms.system.core.servicesdao.BSAMembershipRepository;
import com.sprms.system.master.dao.BSARegistrationRepository;
import com.sprms.system.user.dao.UserRepository;
import com.sprms.system.core.services.SupportingFilesServices;
import com.sprms.system.frmbeans.BSAFundRequestDTO;
import com.sprms.system.frmbeans.BSAFundRequestFormBean;
import com.sprms.system.frmbeans.FundRequestVerificationDTO;
import com.sprms.system.frmbeans.BSAFundRequestDocumentDTO;
import com.sprms.system.frmbeans.BSAFundRequestParticipantDTO;
import com.sprms.system.hbmbeans.BSAFundRequest;
import com.sprms.system.hbmbeans.BSAFundRequestDocument;
import com.sprms.system.hbmbeans.BSAFundRequestParticipant;
import com.sprms.system.hbmbeans.FundRequestStatus;
import com.sprms.system.hbmbeans.DocumentType;
import com.sprms.system.hbmbeans.BSA;
import com.sprms.system.hbmbeans.User;
import com.sprms.system.hbmbeans.MembershipStatus;

@Service
@Transactional
public class BSAFundRequestService {

    private static final Logger logger = LoggerFactory.getLogger(BSAFundRequestService.class);

    private final BSAFundRequestRepository fundRequestRepository;
    private final BSAFundRequestDocumentRepository documentRepository;
    private final BSAFundRequestParticipantRepository participantRepository;
    private final BSAMembershipRepository bsaMembershipRepository;
    private final BSARegistrationRepository bsaRepository;
    private final UserRepository userRepository;
    private final SupportingFilesServices supportingFilesServices;
    // private final NotificationService notificationService;
    private final ReferenceNumberService referenceNumberService;
    
    private final String uploadDir = "uploads/bsa-fund-request/";

    public BSAFundRequestService(
            BSAFundRequestRepository fundRequestRepository,
            BSAFundRequestDocumentRepository documentRepository,
            BSAFundRequestParticipantRepository participantRepository,
            BSAMembershipRepository bsaMembershipRepository,
            BSARegistrationRepository bsaRepository,
            UserRepository userRepository,
            SupportingFilesServices supportingFilesServices,
            // NotificationService notificationService,
            ReferenceNumberService referenceNumberService) {
        this.fundRequestRepository = fundRequestRepository;
        this.documentRepository = documentRepository;
        this.participantRepository = participantRepository;
        this.bsaMembershipRepository = bsaMembershipRepository;
        this.bsaRepository = bsaRepository;
        this.userRepository = userRepository;
        this.supportingFilesServices = supportingFilesServices;
        // this.notificationService = notificationService;
        this.referenceNumberService = referenceNumberService;
    }

    // FR-BSA-06: Submit fund request
    public BSAFundRequestDTO submitFundRequest(BSAFundRequestFormBean formBean, Long requestedByUserId) {
        try {
            logger.info("Submitting BSA fund request for user ID: {}", requestedByUserId);

            // Validate mandatory fields
            validateMandatoryFields(formBean);

            // Validate BSA exists
            BSA bsa = bsaRepository.findById(formBean.getBsaId())
                    .orElseThrow(() -> new RuntimeException("BSA not found"));

            // Validate user exists
            User requestedBy = userRepository.findById(requestedByUserId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Create fund request
            BSAFundRequest fundRequest = new BSAFundRequest(
                    bsa,
                    requestedBy,
                    formBean.getActivityDescription(),
                    formBean.getEventType(),
                    formBean.getJointAccountDetails(),
                    formBean.getBankDetails(),
                    formBean.getRequestedAmount()
            );

            fundRequest.setReferenceNumber(referenceNumberService.generateFundReferenceNumber());
            fundRequest.setCreatedBy(requestedBy.getUsername());
            fundRequest = fundRequestRepository.save(fundRequest);

            // Handle document uploads
            handleDocumentUploads(fundRequest, formBean, requestedByUserId);

            // Handle participant list
            handleParticipantList(fundRequest, formBean);

            // Trigger notification for fund request submission
            // notificationService.notifyFundRequestSubmission(fundRequest);

            logger.info("Successfully submitted BSA fund request with ID: {}", fundRequest.getFundRequestId());
            return convertToDTO(fundRequest);

        } catch (Exception e) {
            logger.error("Error submitting BSA fund request: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to submit fund request: " + e.getMessage());
        }
    }

    // Handle document uploads
    private void handleDocumentUploads(BSAFundRequest fundRequest, BSAFundRequestFormBean formBean, Long uploadedBy) {
        try {
            // Upload proposal document
            if (formBean.getProposalDocumentFile() != null && !formBean.getProposalDocumentFile().isEmpty()) {
                uploadDocument(fundRequest, formBean.getProposalDocumentFile(), DocumentType.PROPOSAL_DOCUMENT, uploadedBy);
            }

            // Upload participant list
            if (formBean.getParticipantListFile() != null && !formBean.getParticipantListFile().isEmpty()) {
                uploadDocument(fundRequest, formBean.getParticipantListFile(), DocumentType.PARTICIPANT_LIST, uploadedBy);
            }

            // Upload BSA registration document
            if (formBean.getBsaRegistrationFile() != null && !formBean.getBsaRegistrationFile().isEmpty()) {
                uploadDocument(fundRequest, formBean.getBsaRegistrationFile(), DocumentType.BSA_REGISTRATION, uploadedBy);
            }

            // Upload supporting document
            if (formBean.getSupportingDocumentFile() != null && !formBean.getSupportingDocumentFile().isEmpty()) {
                uploadDocument(fundRequest, formBean.getSupportingDocumentFile(), DocumentType.SUPPORTING_DOCUMENT, uploadedBy);
            }

        } catch (Exception e) {
            logger.error("Error handling document uploads: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to upload documents: " + e.getMessage());
        }
    }

    // Upload individual document
    private void uploadDocument(BSAFundRequest fundRequest, MultipartFile file, DocumentType documentType, Long uploadedBy) {
        try {
            String fileName = saveUploadedFile(file);
            
            BSAFundRequestDocument document = new BSAFundRequestDocument(
                    fundRequest,
                    documentType,
                    file.getOriginalFilename(),
                    file.getOriginalFilename(),
                    fileName,
                    file.getSize(),
                    file.getContentType(),
                    uploadedBy
            );

            documentRepository.save(document);
            logger.info("Uploaded document: {} for fund request: {}", documentType, fundRequest.getFundRequestId());

        } catch (Exception e) {
            logger.error("Error uploading document {}: {}", documentType, e.getMessage(), e);
            throw new RuntimeException("Failed to upload document: " + e.getMessage());
        }
    }

    /**
     * Save uploaded file
     */
    private String saveUploadedFile(MultipartFile file) {
        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);
            
            return fileName;
        } catch (IOException e) {
            logger.error("Failed to save uploaded file", e);
            throw new RuntimeException("Failed to save uploaded file", e);
        }
    }

    // Handle participant list
    private void handleParticipantList(BSAFundRequest fundRequest, BSAFundRequestFormBean formBean) {
        try {
            if (formBean.getParticipants() != null && !formBean.getParticipants().isEmpty()) {
                for (BSAFundRequestFormBean.ParticipantFormBean participantBean : formBean.getParticipants()) {
                    if (participantBean.getParticipantName() != null && !participantBean.getParticipantName().trim().isEmpty()) {
                        BSAFundRequestParticipant participant = new BSAFundRequestParticipant(
                                fundRequest,
                                participantBean.getParticipantName(),
                                participantBean.getParticipantCid(),
                                participantBean.getContactNumber(),
                                participantBean.getEmailAddress(),
                                participantBean.getRoleDesignation(),
                                participantBean.getCollegeDepartment()
                        );

                        participantRepository.save(participant);
                    }
                }
                logger.info("Saved {} participants for fund request: {}", formBean.getParticipants().size(), fundRequest.getFundRequestId());
            }
        } catch (Exception e) {
            logger.error("Error handling participant list: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to save participant list: " + e.getMessage());
        }
    }

    // FR-BSA-07: Get pending fund requests for focal officer verification
    public Page<BSAFundRequestDTO> getPendingFundRequestsForVerification(Pageable pageable) {
        try {
            logger.info("Getting pending fund requests for verification");
            Page<BSAFundRequest> fundRequests = fundRequestRepository.findByFundRequestStatusOrderByRequestDateDesc(FundRequestStatus.PENDING, pageable);
            logger.info("Found {} pending fund requests", fundRequests.getTotalElements());
            
            // Debug: Log each request found
            fundRequests.forEach(fr -> {
                logger.info("Pending request ID: {}, Status: {}, Description: {}", 
                    fr.getFundRequestId(), fr.getFundRequestStatus(), fr.getActivityDescription());
            });
            
            return fundRequests.map(this::convertToDTO);
        } catch (Exception e) {
            logger.error("Error getting pending fund requests: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to get pending fund requests: " + e.getMessage());
        }
    }

    // FR-BSA-07: Process focal officer verification
    public BSAFundRequestDTO processFocalVerification(FundRequestVerificationDTO verificationDTO, Long verifiedByUserId) {
        try {
            logger.info("Processing focal verification for fund request ID: {}", verificationDTO.getFundRequestId());

            BSAFundRequest fundRequest = fundRequestRepository.findById(verificationDTO.getFundRequestId())
                    .orElseThrow(() -> new RuntimeException("Fund request not found"));

            User verifiedBy = userRepository.findById(verifiedByUserId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Update fund request based on verification decision
            if ("VERIFIED".equals(verificationDTO.getVerifierStatus())) {
                fundRequest.setFundRequestStatus(FundRequestStatus.FOCAL_VERIFIED);
                fundRequest.setApprovedAmount(verificationDTO.getApprovedAmount());
            } else {
                fundRequest.setFundRequestStatus(FundRequestStatus.FOCAL_REJECTED);
            }

            fundRequest.setFocalVerificationDate(LocalDateTime.now());
            fundRequest.setFocalVerifiedBy(verifiedBy);
            fundRequest.setFocalRemarks(verificationDTO.getVerifierRemarks());
            fundRequest.setUpdatedBy(verifiedBy.getUsername());
            fundRequest.setUpdatedDate(LocalDateTime.now());

            fundRequest = fundRequestRepository.save(fundRequest);

            // Trigger notification for focal verification
            // if (fundRequest.getFundRequestStatus() == FundRequestStatus.FOCAL_VERIFIED) {
            //     notificationService.notifyFundRequestApproval(fundRequest);
            // } else {
            //     notificationService.notifyFundRequestRejection(fundRequest);
            // }

            logger.info("Successfully processed focal verification for fund request ID: {}", fundRequest.getFundRequestId());
            return convertToDTO(fundRequest);

        } catch (Exception e) {
            logger.error("Error processing focal verification: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to process focal verification: " + e.getMessage());
        }
    }

    // FR-BSA-08: Get verified fund requests for chief approval
    public Page<BSAFundRequestDTO> getVerifiedFundRequestsForChiefApproval(Pageable pageable) {
        try {
            logger.info("Getting verified fund requests for chief approval");
            Page<BSAFundRequest> fundRequests = fundRequestRepository.findVerifiedForChiefApproval(FundRequestStatus.FOCAL_VERIFIED, pageable);
            logger.info("Found {} verified fund requests for chief approval", fundRequests.getTotalElements());
            
            // Debug: Log each request found
            fundRequests.forEach(fr -> {
                logger.info("Chief approval request ID: {}, Status: {}, Description: {}", 
                    fr.getFundRequestId(), fr.getFundRequestStatus(), fr.getActivityDescription());
            });
            
            return fundRequests.map(this::convertToDTO);
        } catch (Exception e) {
            logger.error("Error getting verified fund requests for chief approval: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to get verified fund requests: " + e.getMessage());
        }
    }

    // FR-BSA-08: Process chief approval
    public BSAFundRequestDTO processChiefApproval(FundRequestVerificationDTO approvalDTO, Long approvedByUserId) {
        try {
            logger.info("Processing chief approval for fund request ID: {}", approvalDTO.getFundRequestId());

            BSAFundRequest fundRequest = fundRequestRepository.findById(approvalDTO.getFundRequestId())
                    .orElseThrow(() -> new RuntimeException("Fund request not found"));

            User approvedBy = userRepository.findById(approvedByUserId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Update fund request based on approval decision
            if ("APPROVED".equals(approvalDTO.getVerifierStatus())) {
                fundRequest.setFundRequestStatus(FundRequestStatus.CHIEF_APPROVED);
                if (approvalDTO.getApprovedAmount() != null) {
                    fundRequest.setApprovedAmount(approvalDTO.getApprovedAmount());
                }
            } else {
                fundRequest.setFundRequestStatus(FundRequestStatus.CHIEF_REJECTED);
            }

            fundRequest.setChiefApprovalDate(LocalDateTime.now());
            fundRequest.setChiefApprovedBy(approvedBy);
            fundRequest.setChiefRemarks(approvalDTO.getVerifierRemarks());
            fundRequest.setUpdatedBy(approvedBy.getUsername());
            fundRequest.setUpdatedDate(LocalDateTime.now());

            fundRequest = fundRequestRepository.save(fundRequest);

            // Trigger notification for chief approval
            // if (fundRequest.getFundRequestStatus() == FundRequestStatus.CHIEF_APPROVED) {
            //     notificationService.notifyFundRequestApproval(fundRequest);
            // } else {
            //     notificationService.notifyFundRequestRejection(fundRequest);
            // }

            logger.info("Successfully processed chief approval for fund request ID: {}", fundRequest.getFundRequestId());
            return convertToDTO(fundRequest);

        } catch (Exception e) {
            logger.error("Error processing chief approval: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to process chief approval: " + e.getMessage());
        }
    }

    // FR-BSA-08: Get chief approved fund requests for accountant approval
    public Page<BSAFundRequestDTO> getChiefApprovedFundRequestsForAccountant(Pageable pageable) {
        try {
            logger.info("Getting chief approved fund requests for accountant approval");
            Page<BSAFundRequest> fundRequests = fundRequestRepository.findChiefApprovedForAccountant(FundRequestStatus.CHIEF_APPROVED, pageable);
            logger.info("Found {} chief approved fund requests for accountant", fundRequests.getTotalElements());
            
            // Debug: Log each request found
            fundRequests.forEach(fr -> {
                logger.info("Accountant approval request ID: {}, Status: {}, Description: {}", 
                    fr.getFundRequestId(), fr.getFundRequestStatus(), fr.getActivityDescription());
            });
            
            return fundRequests.map(this::convertToDTO);
        } catch (Exception e) {
            logger.error("Error getting chief approved fund requests for accountant: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to get chief approved fund requests: " + e.getMessage());
        }
    }

    // FR-BSA-08: Process accountant approval
    public BSAFundRequestDTO processAccountantApproval(FundRequestVerificationDTO approvalDTO, Long approvedByUserId) {
        try {
            logger.info("Processing accountant approval for fund request ID: {}", approvalDTO.getFundRequestId());

            BSAFundRequest fundRequest = fundRequestRepository.findById(approvalDTO.getFundRequestId())
                    .orElseThrow(() -> new RuntimeException("Fund request not found"));

            User approvedBy = userRepository.findById(approvedByUserId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Update fund request based on approval decision
            if ("APPROVED".equals(approvalDTO.getVerifierStatus())) {
                fundRequest.setFundRequestStatus(FundRequestStatus.ACCOUNTANT_APPROVED);
                if (approvalDTO.getApprovedAmount() != null) {
                    fundRequest.setApprovedAmount(approvalDTO.getApprovedAmount());
                }
            } else {
                fundRequest.setFundRequestStatus(FundRequestStatus.ACCOUNTANT_REJECTED);
            }

            fundRequest.setAccountantApprovalDate(LocalDateTime.now());
            fundRequest.setAccountantApprovedBy(approvedBy);
            fundRequest.setAccountantRemarks(approvalDTO.getVerifierRemarks());
            fundRequest.setUpdatedBy(approvedBy.getUsername());
            fundRequest.setUpdatedDate(LocalDateTime.now());

            fundRequest = fundRequestRepository.save(fundRequest);

            // Trigger notification for accountant approval
            // if (fundRequest.getFundRequestStatus() == FundRequestStatus.ACCOUNTANT_APPROVED) {
            //     notificationService.notifyFundRequestApproval(fundRequest);
            // } else {
            //     notificationService.notifyFundRequestRejection(fundRequest);
            // }

            logger.info("Successfully processed accountant approval for fund request ID: {}", fundRequest.getFundRequestId());
            return convertToDTO(fundRequest);

        } catch (Exception e) {
            logger.error("Error processing accountant approval: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to process accountant approval: " + e.getMessage());
        }
    }

    // Get fund request by ID with documents and participants
    public BSAFundRequestDTO getFundRequestById(Long fundRequestId) {
        try {
            BSAFundRequest fundRequest = fundRequestRepository.findById(fundRequestId)
                    .orElseThrow(() -> new RuntimeException("Fund request not found"));

            return convertToDTO(fundRequest);
        } catch (Exception e) {
            logger.error("Error getting fund request by ID: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to get fund request: " + e.getMessage());
        }
    }

    // Get fund requests by user
    public Page<BSAFundRequestDTO> getFundRequestsByUser(Long userId, Pageable pageable) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Page<BSAFundRequest> fundRequests = fundRequestRepository.findByRequestedBy(user, pageable);
            return fundRequests.map(this::convertToDTO);
        } catch (Exception e) {
            logger.error("Error getting fund requests by user: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to get fund requests by user: " + e.getMessage());
        }
    }

    // Get fund request statistics
    public List<Object[]> getFundRequestStatistics() {
        try {
            return fundRequestRepository.getFundRequestStatistics();
        } catch (Exception e) {
            logger.error("Error getting fund request statistics: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to get fund request statistics: " + e.getMessage());
        }
    }

    // Convert entity to DTO
    private BSAFundRequestDTO convertToDTO(BSAFundRequest fundRequest) {
        BSAFundRequestDTO dto = new BSAFundRequestDTO();
        dto.setFundRequestId(fundRequest.getFundRequestId());
        dto.setBsaName(fundRequest.getBsa() != null ? fundRequest.getBsa().getBsaName() : null);
        dto.setBsaCode(fundRequest.getBsa() != null ? fundRequest.getBsa().getBsaCode() : null);
        dto.setRequestedByName(fundRequest.getRequestedBy() != null ? 
                fundRequest.getRequestedBy().getFirstname() + " " + fundRequest.getRequestedBy().getLastname() : null);
        dto.setRequestedByCid(fundRequest.getRequestedBy() != null ? fundRequest.getRequestedBy().getCidno() : null);
        dto.setReferenceNumber(fundRequest.getReferenceNumber());
        dto.setActivityDescription(fundRequest.getActivityDescription());
        dto.setEventType(fundRequest.getEventType() != null ? fundRequest.getEventType().toString() : null);
        dto.setJointAccountDetails(fundRequest.getJointAccountDetails());
        dto.setBankDetails(fundRequest.getBankDetails());
        dto.setRequestedAmount(fundRequest.getRequestedAmount());
        dto.setApprovedAmount(fundRequest.getApprovedAmount());
        dto.setFundRequestStatus(fundRequest.getFundRequestStatus().toString());
        dto.setRequestDate(fundRequest.getRequestDate());
        dto.setFocalVerificationDate(fundRequest.getFocalVerificationDate());
        dto.setFocalVerifiedByName(fundRequest.getFocalVerifiedBy() != null ?
                fundRequest.getFocalVerifiedBy().getFirstname() + " " + fundRequest.getFocalVerifiedBy().getLastname() : null);
        dto.setFocalRemarks(fundRequest.getFocalRemarks());
        dto.setChiefApprovalDate(fundRequest.getChiefApprovalDate());
        dto.setChiefApprovedByName(fundRequest.getChiefApprovedBy() != null ?
                fundRequest.getChiefApprovedBy().getFirstname() + " " + fundRequest.getChiefApprovedBy().getLastname() : null);
        dto.setChiefRemarks(fundRequest.getChiefRemarks());
        dto.setAccountantApprovalDate(fundRequest.getAccountantApprovalDate());
        dto.setAccountantApprovedByName(fundRequest.getAccountantApprovedBy() != null ?
                fundRequest.getAccountantApprovedBy().getFirstname() + " " + fundRequest.getAccountantApprovedBy().getLastname() : null);
        dto.setAccountantRemarks(fundRequest.getAccountantRemarks());

        // Load documents
        List<BSAFundRequestDocument> documents = documentRepository.findByFundRequest(fundRequest);
        dto.setDocuments(documents.stream().map(this::convertDocumentToDTO).collect(Collectors.toList()));

        // Load participants
        List<BSAFundRequestParticipant> participants = participantRepository.findByFundRequest(fundRequest);
        dto.setParticipants(participants.stream().map(this::convertParticipantToDTO).collect(Collectors.toList()));

        return dto;
    }

    // Convert document entity to DTO
    private BSAFundRequestDocumentDTO convertDocumentToDTO(BSAFundRequestDocument document) {
        BSAFundRequestDocumentDTO dto = new BSAFundRequestDocumentDTO();
        dto.setDocumentId(document.getDocumentId());
        dto.setDocumentType(document.getDocumentType().toString());
        dto.setDocumentName(document.getDocumentName());
        dto.setOriginalFileName(document.getOriginalFileName());
        dto.setFilePath(document.getFilePath());
        dto.setFileSize(document.getFileSize());
        dto.setMimeType(document.getMimeType());
        dto.setUploadDate(document.getUploadDate());
        // Note: uploadedByName would need additional query to get user details
        return dto;
    }

    // Convert participant entity to DTO
    private BSAFundRequestParticipantDTO convertParticipantToDTO(BSAFundRequestParticipant participant) {
        BSAFundRequestParticipantDTO dto = new BSAFundRequestParticipantDTO();
        dto.setParticipantId(participant.getParticipantId());
        dto.setParticipantName(participant.getParticipantName());
        dto.setParticipantCid(participant.getParticipantCid());
        dto.setContactNumber(participant.getContactNumber());
        dto.setEmailAddress(participant.getEmailAddress());
        dto.setRoleDesignation(participant.getRoleDesignation());
        dto.setCollegeDepartment(participant.getCollegeDepartment());
        dto.setCreatedDate(participant.getCreatedDate());
        return dto;
    }
    
    // Get participant count based on BSA membership
    public long getParticipantCountByBsa(Long bsaId) {
        logger.info("Getting participant count for BSA ID: {}", bsaId);
        long count = bsaMembershipRepository.countByBsaIdAndMembershipStatus(bsaId, MembershipStatus.APPROVED);
        logger.info("Found {} verified members for BSA ID: {}", count, bsaId);
        return count;
    }
    
    // Get all fund requests (for dashboard)
    public Page<BSAFundRequestDTO> getAllFundRequests(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<BSAFundRequest> fundRequests = fundRequestRepository.findAll(pageable);
            return fundRequests.map(this::convertToDTO);
        } catch (Exception e) {
            logger.error("Error fetching all fund requests: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch fund requests: " + e.getMessage());
        }
    }

    
    // Get fund request count by status
    public long getFundRequestCountByStatus(FundRequestStatus status) {
        try {
            return fundRequestRepository.countByFundRequestStatus(status);
        } catch (Exception e) {
            logger.error("Error counting fund requests by status: {}", e.getMessage(), e);
            return 0;
        }
    }

    // Validate mandatory fields and documents for fund request submission
    private void validateMandatoryFields(BSAFundRequestFormBean formBean) {
        if (formBean.getBsaId() == null) {
            throw new IllegalArgumentException("BSA selection is required");
        }
        
        if (formBean.getEventType() == null) {
            throw new IllegalArgumentException("Event type selection is required");
        }
        
        if (formBean.getActivityDescription() == null || formBean.getActivityDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Activity description is required");
        }
        
        if (formBean.getJointAccountDetails() == null || formBean.getJointAccountDetails().trim().isEmpty()) {
            throw new IllegalArgumentException("Joint account details are required");
        }
        
        if (formBean.getBankDetails() == null || formBean.getBankDetails().trim().isEmpty()) {
            throw new IllegalArgumentException("Bank details are required");
        }
        
        if (formBean.getRequestedAmount() == null || formBean.getRequestedAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Requested amount must be greater than 0");
        }
        
        // Validate mandatory document uploads
        logger.info("Checking proposal document file: {}", formBean.getProposalDocumentFile());
        if (formBean.getProposalDocumentFile() != null) {
            logger.info("Proposal file name: {}, size: {}, empty: {}", 
                formBean.getProposalDocumentFile().getOriginalFilename(),
                formBean.getProposalDocumentFile().getSize(),
                formBean.getProposalDocumentFile().isEmpty());
        }
        
        if (formBean.getProposalDocumentFile() == null || formBean.getProposalDocumentFile().isEmpty()) {
            throw new IllegalArgumentException("Proposal document upload is required");
        }
    }

    // Find fund request by reference number for tracking
    public BSAFundRequest findByReferenceNumber(String referenceNumber) {
        logger.info("Searching for fund request with reference number: {}", referenceNumber);
        return fundRequestRepository.findByReferenceNumber(referenceNumber)
                .orElseThrow(() -> new RuntimeException("Fund request not found with reference number: " + referenceNumber));
    }
}
