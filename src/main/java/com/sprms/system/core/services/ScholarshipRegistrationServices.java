package com.sprms.system.core.services;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sprms.system.applicationEnums.ApplicationStatus;
import com.sprms.system.core.servicesdao.ScholarshipRegistrationRepository;
import com.sprms.system.frmbeans.ApproverActionDTO;
import com.sprms.system.frmbeans.ScholarshipRegistrationDTO;
import com.sprms.system.frmbeans.VerifierActionDTO;
import com.sprms.system.hbmbeans.ScholarshipRegistration;
import com.sprms.system.modelMapper.ScholarshipRegistrationDTOMapper;
import com.sprms.system.utils.DateUtil;

import org.springframework.security.core.context.SecurityContextHolder;

//this is created to get all the application for the verification ans approval for Foal persoon
//date 13/04/2026
//place : YK

@Service
public class ScholarshipRegistrationServices {

	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(ScholarshipRegistrationServices.class);

	// call the Repository
	private final ScholarshipRegistrationRepository _scholarshipRegistrationRepository;
	private final ScholarshipRegistrationDTOMapper _scholarshipRegistrationDTOMapper;

	// Call email service
	private final EmailServices _emailServices;

	// constructor
	public ScholarshipRegistrationServices(ScholarshipRegistrationRepository scholarshipRegistrationRepository,
			ScholarshipRegistrationDTOMapper scholarshipRegistrationDTOMapper, EmailServices emailServices) {
		this._scholarshipRegistrationRepository = scholarshipRegistrationRepository;
		this._scholarshipRegistrationDTOMapper = scholarshipRegistrationDTOMapper;
		this._emailServices = emailServices;
	}

	// Load the Information in the Page
	public Page<ScholarshipRegistrationDTO> getApplicationByStatus(ApplicationStatus status, int page, int size) {

		logger.info("@@@Calling the getApplicationByStatus proc..................");

		Pageable pageable = PageRequest.of(page, size);

		Page<ScholarshipRegistration> applstresult = _scholarshipRegistrationRepository.findByStatus(status, pageable);

		return applstresult.map(ScholarshipRegistrationDTOMapper::toDTO);
	}

	// get the Application Details by Its Application Id
	public ScholarshipRegistrationDTO getById(Long id) {

		ScholarshipRegistration entity = _scholarshipRegistrationRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Record not found"));

		return ScholarshipRegistrationDTOMapper.toDTO(entity);
	}

	// update the verifier status for the particular application
	// date 14/04/2026
	public void updateVerifierAction(VerifierActionDTO dto) {

		logger.info("@@@Calling the updateVerifierAction proc...........");

		ScholarshipRegistration app = _scholarshipRegistrationRepository.findById(dto.getApplicationId())
				.orElseThrow(() -> new RuntimeException("Application not found"));

		// Only update required fields
		app.setStatus(dto.getVerifierStatus());
		app.setVerifier_remarks(dto.getVerifierRemarks());
		app.setVerified_at(DateUtil.getCurrentDateTime());

		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		app.setVerified_by(username);

		_scholarshipRegistrationRepository.save(app);
		
	    //TRIGGER EMAIL ON APPLICATION REJECT
	    if (dto.getVerifierStatus() == ApplicationStatus.REJECTED) {
	        _emailServices.sendRejectionEmail(
	                app.getEmailAddress(),
	                app.getFirstName(),
	                app.getId(),
	                dto.getVerifierRemarks()
	        );
	    }

	}

	// update the verifier status for the particular application
	// date 14/04/2026
	public void updateApproverAction(ApproverActionDTO dto) {

		logger.info("@@@Calling the updateApproverAction proc...........");

		ScholarshipRegistration app = _scholarshipRegistrationRepository.findById(dto.getApplicationId())
				.orElseThrow(() -> new RuntimeException("Application not found"));

		// Only update required fields
		app.setStatus(dto.getApprovalStatus());
		app.setApproval_remarks(dto.getApprovalRemarks());
		app.setApproved_at(DateUtil.getCurrentDateTime());

		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		app.setApproved_by(username);

		_scholarshipRegistrationRepository.save(app);

	}

	// Search/Track Application for status
	// created 15/04/2026
	public List<ScholarshipRegistrationDTO> getByCid(String citizenId) {

		logger.info("@@@Calling the getByCid proc.................");

		List<ScholarshipRegistration> registrations = _scholarshipRegistrationRepository.findByCitizenId(citizenId);

		return ScholarshipRegistrationDTOMapper.toDTOList(registrations);
	}
}
