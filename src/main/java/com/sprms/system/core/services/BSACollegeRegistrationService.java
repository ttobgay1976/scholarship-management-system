package com.sprms.system.core.services;

import com.sprms.system.core.servicesdao.BSACollegeRegistrationRepository;
import com.sprms.system.master.dao.BSARegistrationRepository;
import com.sprms.system.core.servicesdao.BSACollegeRegistrationRepository;
import com.sprms.system.frmbeans.BSACollegeRegistrationDTO;
import com.sprms.system.frmbeans.BSACollegeRegistrationFormBean;
import com.sprms.system.hbmbeans.*;
import com.sprms.system.hbmbeans.BSAStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class BSACollegeRegistrationService {

    @Autowired
    private BSACollegeRegistrationRepository registrationRepository;

    @Autowired
    private BSARegistrationRepository bsaRepository;

    // Get all registrations for a specific BSA
    @Transactional(readOnly = true)
    public List<BSACollegeRegistrationDTO> getRegistrationsByBSA(Long bsaId) {
        List<BSACollegeRegistration> registrations = registrationRepository.findByBsaBsaId(bsaId);
        return registrations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    
    // Get all registrations (for general list page)
    @Transactional(readOnly = true)
    public List<BSACollegeRegistrationDTO> getAllRegistrations() {
        List<BSACollegeRegistration> registrations = registrationRepository.findAll();
        return registrations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get total registration count
    @Transactional(readOnly = true)
    public Long getTotalRegistrationCount() {
        return registrationRepository.count();
    }

    
    // Get all colleges for BSA registration by location (from college table)
    @Transactional(readOnly = true)
    public List<BSACollegeRegistrationDTO.CollegeInfo> getCollegesByLocation(Long countryId, Long stateId, Long cityId) {
        List<College> colleges = registrationRepository.findCollegesByLocation(countryId, stateId, cityId);
        return colleges.stream()
                .map(college -> new BSACollegeRegistrationDTO.CollegeInfo(
                        college.getId(),
                        college.getCollegeName(),
                        college.getShortName()
                ))
                .collect(Collectors.toList());
    }

    // Get available colleges for BSA registration by location (excluding already registered)
    @Transactional(readOnly = true)
    public List<BSACollegeRegistrationDTO.CollegeInfo> getAvailableCollegesForBSA(Long bsaId, Long countryId, Long stateId, Long cityId) {
        List<College> availableColleges = registrationRepository.findAvailableCollegesForBSAByLocation(bsaId, countryId, stateId, cityId);
        return availableColleges.stream()
                .map(college -> new BSACollegeRegistrationDTO.CollegeInfo(
                        college.getId(),
                        college.getCollegeName(),
                        college.getShortName()
                ))
                .collect(Collectors.toList());
    }

    
    // Register multiple colleges to a BSA
    public List<BSACollegeRegistrationDTO> registerCollegesToBSA(BSACollegeRegistrationFormBean formBean, String username) {
        List<BSACollegeRegistration> registrations = formBean.getSelectedCollegeIds().stream()
                .map(collegeId -> {
                    BSACollegeRegistration registration = new BSACollegeRegistration();
                    
                    // Set BSA
                    BSA bsa = bsaRepository.findById(formBean.getBsaId())
                            .orElseThrow(() -> new RuntimeException("BSA not found with ID: " + formBean.getBsaId()));
                    registration.setBsa(bsa);
                    
                    // Set College - use BSACollegeRegistrationRepository to find College
                    College college = registrationRepository.findCollegeById(collegeId)
                            .orElseThrow(() -> new RuntimeException("College not found with ID: " + collegeId));
                    if (college == null) {
                        throw new RuntimeException("College not found with ID: " + collegeId);
                    }
                    registration.setCollege(college);
                    
                    // Set location details from college
                    registration.setCountry(college.getCountry());
                    registration.setState(college.getState());
                    registration.setCity(college.getCity());
                    
                    // Set audit fields
                    registration.setCreatedDate(LocalDateTime.now());
                    registration.setCreatedBy(username);
                    
                    // Set remarks if provided
                    if (formBean.getRemarks() != null && !formBean.getRemarks().trim().isEmpty()) {
                        registration.setRemarks(formBean.getRemarks());
                    }
                    
                    return registration;
                })
                .collect(Collectors.toList());

        List<BSACollegeRegistration> savedRegistrations = registrationRepository.saveAll(registrations);
        return savedRegistrations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    
    // Delete registration
    public void deleteRegistration(Long registrationId) {
        if (!registrationRepository.existsById(registrationId)) {
            throw new RuntimeException("Registration not found with ID: " + registrationId);
        }
        registrationRepository.deleteById(registrationId);
    }

    // Delete registration by BSA and College
    public void deleteRegistrationByBSAAndCollege(Long bsaId, Long collegeId) {
        registrationRepository.deleteByBsaBsaIdAndCollegeId(bsaId, collegeId);
    }

    // Check if college is already registered with BSA
    @Transactional(readOnly = true)
    public boolean isCollegeRegisteredWithBSA(Long bsaId, Long collegeId) {
        return registrationRepository.existsByBsaAndCollege(bsaId, collegeId);
    }

    // Get registration count by BSA
    @Transactional(readOnly = true)
    public Long getRegistrationCountByBSA(Long bsaId) {
        return registrationRepository.countByBsa(bsaId);
    }

    
    // Convert entity to DTO
    private BSACollegeRegistrationDTO convertToDTO(BSACollegeRegistration entity) {
        BSACollegeRegistrationDTO dto = new BSACollegeRegistrationDTO();
        
        dto.setRegistrationId(entity.getRegistrationId());
        
        // BSA details
        if (entity.getBsa() != null) {
            dto.setBsaId(entity.getBsa().getBsaId());
            dto.setBsaCode(entity.getBsa().getBsaCode());
            dto.setBsaName(entity.getBsa().getBsaName() != null ? entity.getBsa().getBsaName() : "");
        } else {
            dto.setBsaName("");
        }
        
        // College details
        if (entity.getCollege() != null) {
            dto.setCollegeId(entity.getCollege().getId());
            dto.setCollegeName(entity.getCollege().getCollegeName() != null ? entity.getCollege().getCollegeName() : "");
            dto.setCollegeShortName(entity.getCollege().getShortName() != null ? entity.getCollege().getShortName() : "");
        } else {
            dto.setCollegeName("");
            dto.setCollegeShortName("");
        }
        
        // Location details
        if (entity.getCountry() != null) {
            dto.setCountryId(entity.getCountry().getId());
            dto.setCountryName(entity.getCountry().getCountryName() != null ? entity.getCountry().getCountryName() : "");
        } else {
            dto.setCountryName("");
        }
        
        if (entity.getState() != null) {
            dto.setStateId(entity.getState().getId());
            dto.setStateName(entity.getState().getStateName() != null ? entity.getState().getStateName() : "");
        } else {
            dto.setStateName("");
        }
        
        if (entity.getCity() != null) {
            dto.setCityId(entity.getCity().getId());
            dto.setCityName(entity.getCity().getCityName() != null ? entity.getCity().getCityName() : "");
        } else {
            dto.setCityName("");
        }
        
        // Registration details
        dto.setRegistrationDate(entity.getRegistrationDate());
        dto.setStatus(entity.getStatus() != null ? entity.getStatus().toString() : "");
        dto.setRemarks(entity.getRemarks() != null ? entity.getRemarks() : "");
        
        // Audit fields
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setUpdatedDate(entity.getUpdatedDate());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setUpdatedBy(entity.getUpdatedBy());
        
        return dto;
    }

    // Prepare form bean for create page
    @Transactional(readOnly = true)
    public BSACollegeRegistrationFormBean prepareCreateFormBean(Long bsaId) {
        BSACollegeRegistrationFormBean formBean = new BSACollegeRegistrationFormBean();
        
        Optional<BSA> bsaOpt = bsaRepository.findById(bsaId);
        if (bsaOpt.isPresent()) {
            BSA bsa = bsaOpt.get();
            formBean.setBsaId(bsa.getBsaId());
            formBean.setBsaName(bsa.getBsaName());
        }
        
        return formBean;
    }

    // Validate form bean
    public boolean validateFormBean(BSACollegeRegistrationFormBean formBean) {
        boolean isValid = true;
        
        // Reset validation flags
        formBean.setShowBsaError(false);
        formBean.setShowCountryError(false);
        formBean.setShowStateError(false);
        formBean.setShowCityError(false);
        formBean.setShowCollegeError(false);
        formBean.setErrorMessage(null);
        
        // Validate BSA
        if (formBean.getBsaId() == null) {
            formBean.setShowBsaError(true);
            isValid = false;
        }
        
        // Validate country
        if (formBean.getCountryId() == null) {
            formBean.setShowCountryError(true);
            isValid = false;
        }
        
        // Validate state
        if (formBean.getStateId() == null) {
            formBean.setShowStateError(true);
            isValid = false;
        }
        
        // Validate city
        if (formBean.getCityId() == null) {
            formBean.setShowCityError(true);
            isValid = false;
        }
        
        // Validate college selection
        if (formBean.getSelectedCollegeIds() == null || formBean.getSelectedCollegeIds().isEmpty()) {
            formBean.setShowCollegeError(true);
            isValid = false;
        }
        
        if (!isValid) {
            formBean.setErrorMessage("Please fill in all required fields and select at least one college");
        }
        
        return isValid;
    }

    // Get all active BSAs for selection
    @Transactional(readOnly = true)
    public List<BSA> getAllActiveBSAs() {
        return bsaRepository.findByStatus(BSAStatus.ACTIVE);
    }

    // Update registration status
    @Transactional
    public BSACollegeRegistrationDTO updateRegistrationStatus(Long registrationId, RegistrationStatus status, String username) {
        BSACollegeRegistration registration = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new RuntimeException("Registration not found with ID: " + registrationId));
        
        registration.setStatus(status);
        registration.setUpdatedDate(LocalDateTime.now());
        registration.setUpdatedBy(username);
        
        BSACollegeRegistration savedRegistration = registrationRepository.save(registration);
        return convertToDTO(savedRegistration);
    }

    // Get BSA by ID
    @Transactional(readOnly = true)
    public Optional<BSA> getBSAById(Long bsaId) {
        return bsaRepository.findById(bsaId);
    }
}
