package com.sprms.system.master.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sprms.system.frmbeans.BSARequestDTO;
import com.sprms.system.frmbeans.BSARequestFormBean;
import com.sprms.system.hbmbeans.BSARequest;
import com.sprms.system.hbmbeans.RequestStatus;
import com.sprms.system.master.dao.BSARequestRepository;

@Service
@Transactional
public class BSARequestService {
    
    private static final Logger logger = LoggerFactory.getLogger(BSARequestService.class);
    
    private final BSARequestRepository bsaRequestRepository;
    private final String uploadDir;
    
    public BSARequestService(BSARequestRepository bsaRequestRepository, 
                           @Value("${app.upload.dir:uploads}") String uploadDir) {
        this.bsaRequestRepository = bsaRequestRepository;
        this.uploadDir = uploadDir;
    }
    
    /**
     * Submit a new BSA registration request
     */
    public BSARequestDTO submitRequest(BSARequestFormBean formBean, String submittedBy) {
        logger.info("Submitting BSA registration request for: {}", formBean.getBsaName());
        
        // Validate mandatory fields
        validateMandatoryFields(formBean);
        
        BSARequest request = convertToEntity(formBean);
        request.setSubmittedBy(submittedBy);
        request.setRequestStatus(RequestStatus.PENDING);
        
        // Handle file upload
        if (formBean.getSupportingDocumentFile() != null && !formBean.getSupportingDocumentFile().isEmpty()) {
            String fileName = saveUploadedFile(formBean.getSupportingDocumentFile());
            request.setSupportingDocuments(fileName);
        }
        
        BSARequest savedRequest = bsaRequestRepository.save(request);
        logger.info("BSA request submitted successfully with ID: {}", savedRequest.getRequestId());
        
        return convertToDTO(savedRequest);
    }
    
    /**
     * Get all BSA requests
     */
    public List<BSARequestDTO> getAllRequests() {
        logger.info("Fetching all BSA requests");
        return bsaRequestRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get requests by status
     */
    public List<BSARequestDTO> getRequestsByStatus(RequestStatus status) {
        logger.info("Fetching BSA requests with status: {}", status);
        return bsaRequestRepository.findByRequestStatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get pending requests for approval
     */
    public List<BSARequestDTO> getPendingRequests() {
        logger.info("Fetching pending BSA requests");
        return bsaRequestRepository.findPendingRequests(RequestStatus.PENDING).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get request by ID
     */
    public BSARequestDTO getRequestById(Long requestId) {
        logger.info("Fetching BSA request with ID: {}", requestId);
        return bsaRequestRepository.findById(requestId)
                .map(this::convertToDTO)
                .orElse(null);
    }
    
    /**
     * Approve a BSA request
     */
    public BSARequestDTO approveRequest(Long requestId, String approvedBy, String remarks, RequestStatus status, String approverRemark) {
        logger.info("Processing BSA request with ID: {} to status: {}", requestId, status);
        
        BSARequest request = bsaRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found: " + requestId));
        
        request.setRequestStatus(status);
        request.setApprovedBy(approvedBy);
        request.setApprovedDate(LocalDateTime.now());
        request.setRemarks(remarks);
        // Note: approverRemark would need to be added to BSARequest entity if not already present
        
        BSARequest savedRequest = bsaRequestRepository.save(request);
        logger.info("BSA request processed successfully with status: {}", status);
        
        return convertToDTO(savedRequest);
    }
    
    /**
     * Reject a BSA request
     */
    public BSARequestDTO rejectRequest(Long requestId, String approvedBy, String remarks) {
        logger.info("Rejecting BSA request with ID: {}", requestId);
        
        BSARequest request = bsaRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found: " + requestId));
        
        request.setRequestStatus(RequestStatus.REJECTED);
        request.setApprovedBy(approvedBy);
        request.setApprovedDate(LocalDateTime.now());
        request.setRemarks(remarks);
        
        BSARequest savedRequest = bsaRequestRepository.save(request);
        logger.info("BSA request rejected successfully");
        
        return convertToDTO(savedRequest);
    }
    
    /**
     * Delete a BSA request
     */
    public void deleteRequest(Long requestId) {
        logger.info("Deleting BSA request with ID: {}", requestId);
        bsaRequestRepository.deleteById(requestId);
        logger.info("BSA request deleted successfully");
    }
    
    /**
     * Get request statistics
     */
    public RequestStats getRequestStats() {
        long pending = bsaRequestRepository.countByRequestStatus(RequestStatus.PENDING);
        long approved = bsaRequestRepository.countByRequestStatus(RequestStatus.APPROVED);
        long rejected = bsaRequestRepository.countByRequestStatus(RequestStatus.REJECTED);
        
        return new RequestStats(pending, approved, rejected);
    }
    
    /**
     * Validate mandatory fields
     */
    private void validateMandatoryFields(BSARequestFormBean formBean) {
        if (formBean.getBsaName() == null || formBean.getBsaName().trim().isEmpty()) {
            throw new IllegalArgumentException("BSA name is required");
        }
        
        if (formBean.getPresidentCid() == null || formBean.getPresidentCid().trim().isEmpty()) {
            throw new IllegalArgumentException("President CID is required");
        }
        
        if (formBean.getVicePresidentCid() == null || formBean.getVicePresidentCid().trim().isEmpty()) {
            throw new IllegalArgumentException("Vice President CID is required");
        }
        
        if (formBean.getCountryId() == null) {
            throw new IllegalArgumentException("Country selection is required");
        }
        
        if (formBean.getStateId() == null) {
            throw new IllegalArgumentException("State selection is required");
        }
        
        if (formBean.getCityId() == null) {
            throw new IllegalArgumentException("City selection is required");
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
    
    /**
     * Convert FormBean to Entity
     */
    private BSARequest convertToEntity(BSARequestFormBean formBean) {
        BSARequest request = new BSARequest();
        request.setRequestId(formBean.getRequestId());
        request.setBsaName(formBean.getBsaName());
        request.setCountryId(formBean.getCountryId());
        request.setStateId(formBean.getStateId());
        request.setCityId(formBean.getCityId());
        request.setPresidentCid(formBean.getPresidentCid());
        request.setVicePresidentCid(formBean.getVicePresidentCid());
        request.setDescription(formBean.getDescription());
        return request;
    }
    
    /**
     * Convert Entity to DTO
     */
    private BSARequestDTO convertToDTO(BSARequest request) {
        BSARequestDTO dto = new BSARequestDTO();
        dto.setRequestId(request.getRequestId());
        dto.setBsaName(request.getBsaName());
        dto.setCountryId(request.getCountryId());
        dto.setStateId(request.getStateId());
        dto.setCityId(request.getCityId());
        dto.setPresidentCid(request.getPresidentCid());
        dto.setVicePresidentCid(request.getVicePresidentCid());
        dto.setDescription(request.getDescription());
        dto.setSupportingDocuments(request.getSupportingDocuments());
        dto.setRequestStatus(request.getRequestStatus());
        dto.setSubmittedBy(request.getSubmittedBy());
        dto.setSubmittedDate(request.getSubmittedDate());
        dto.setApprovedBy(request.getApprovedBy());
        dto.setApprovedDate(request.getApprovedDate());
        dto.setRemarks(request.getRemarks());
        
        // Set related entity names
        if (request.getCountry() != null) {
            dto.setCountryName(request.getCountry().getCountryName());
        }
        if (request.getState() != null) {
            dto.setStateName(request.getState().getStateName());
        }
        if (request.getCity() != null) {
            dto.setCityName(request.getCity().getCityName());
        }
        
        return dto;
    }
    
    /**
     * Request Statistics DTO
     */
    public static class RequestStats {
        private final long pending;
        private final long approved;
        private final long rejected;
        
        public RequestStats(long pending, long approved, long rejected) {
            this.pending = pending;
            this.approved = approved;
            this.rejected = rejected;
        }
        
        public long getPending() { return pending; }
        public long getApproved() { return approved; }
        public long getRejected() { return rejected; }
    }
}
