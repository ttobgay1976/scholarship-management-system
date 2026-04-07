package com.sprms.system.master.services;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sprms.system.frmbeans.FundingAgencyDTO;
import com.sprms.system.frmbeans.ScholarshipProgramDTO;
import com.sprms.system.hbmbeans.FundingAgency;
import com.sprms.system.hbmbeans.ScholarshipProgram;
import com.sprms.system.master.dao.FundingAgencyRepository;
import com.sprms.system.master.dao.ScholarshipProgramRepository;
import com.sprms.system.modelMapper.ScholarshipProgramMapper;
import com.sprms.system.utils.DateUtil;

@Service
public class ScholarshipProgramService {

	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(ScholarshipProgramService.class);

	// call reposiroty as a private
	private final ScholarshipProgramRepository _scholarshipProgramRepository;
	private final ScholarshipProgramMapper _scholarshipProgramMapper;
	private final FundingAgencyService _fundingAgencyService;
	private final FundingAgencyRepository _fundingAgencyRepository;

	// constructor
	public ScholarshipProgramService(ScholarshipProgramRepository scholarshipProgramRepository,
			ScholarshipProgramMapper scholarshipProgramMapper,FundingAgencyService fundingAgencyService,FundingAgencyRepository fundingAgencyRepository) {
		this._scholarshipProgramRepository = scholarshipProgramRepository;
		this._scholarshipProgramMapper = scholarshipProgramMapper;
		this._fundingAgencyService=fundingAgencyService;
		this._fundingAgencyRepository=fundingAgencyRepository;
	}

	// Save new scholarship Program 
	public ScholarshipProgramDTO saveScholarshipProgram(ScholarshipProgramDTO dto) {

		logger.info("@@@Calling the saveSchloarshipProgram.............. ");
		
		FundingAgency fndagency = _fundingAgencyRepository.findById(dto.getFundingAgencyId())
				.orElseThrow(() -> new RuntimeException("FundingAgency not found with id: " + dto.getFundingAgencyId()));

		ScholarshipProgram program = _scholarshipProgramMapper.toEntity(dto,fndagency);
		program.setCreatedat(DateUtil.getCurrentDateTime());
		program.setId(Long.parseLong(DateUtil.getUniqueID()));
		
	    // save
	    ScholarshipProgram saved = _scholarshipProgramRepository.save(program);

	    // convert back to DTO
	    return _scholarshipProgramMapper.toDTO(saved);
		
	}

	// Update existing scholarship
	public ScholarshipProgramDTO updateScholarshipProgram(Long id, ScholarshipProgramDTO dto) {

		logger.info("@@@Calling the updateScholarshipProgram.............. ");

		ScholarshipProgram existing = _scholarshipProgramRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Scholarship not found"));

		existing.setScholarshipProgramName(dto.getScholarshipProgramName());
		/* existing.setFundingAgency(dto.getFundingAgencyName()); */
		existing.setScholarshipAvailableSlots(dto.getScholarshipAvailableSlots());
		existing.setUpdateat(DateUtil.getCurrentDateTime());

		return _scholarshipProgramMapper.toDTO(_scholarshipProgramRepository.save(existing));
	}

	// Get by ID
	public ScholarshipProgramDTO getById(Long id) {

		logger.info("@@@Calling the getById.............. ");

		ScholarshipProgram entity = _scholarshipProgramRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Scholarship not found"));
		return _scholarshipProgramMapper.toDTO(entity);
	}

	// Get all
	public List<ScholarshipProgramDTO> getAll() {

		logger.info("@@@Calling the getAll.............. ");

		return _scholarshipProgramRepository.findAll().stream().map(_scholarshipProgramMapper::toDTO)
				.collect(Collectors.toList());
	}

	// Delete
	public void deleteScholarshipProgram(Long id) {

		logger.info("@@@Calling the deleteScholarshipProgram.............. ");

		if (!_scholarshipProgramRepository.existsById(id))
			throw new RuntimeException("Scholarship not found");
		_scholarshipProgramRepository.deleteById(id);
	}

}
