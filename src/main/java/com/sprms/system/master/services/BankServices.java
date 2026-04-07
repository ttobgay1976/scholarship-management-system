package com.sprms.system.master.services;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sprms.system.frmbeans.BankDTO;
import com.sprms.system.frmbeans.CollegesDTO;
import com.sprms.system.frmbeans.FundingAgencyDTO;
import com.sprms.system.hbmbeans.Bank;
import com.sprms.system.hbmbeans.College;
import com.sprms.system.master.dao.BankRepository;
import com.sprms.system.master.dao.CollegeRegistrationRepository;
import com.sprms.system.modelMapper.BankDTOMapper;
import com.sprms.system.modelMapper.CollegesDTOMapper;
import com.sprms.system.utils.DateUtil;

@Service
public class BankServices {

	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(BankServices.class);

	// call the repository
	private final BankRepository _bankRepository;
	private final CollegeRegistrationRepository _collegeRegistrationRepository;

	// constructor
	public BankServices(BankRepository bakBankRepository,CollegeRegistrationRepository collegeRegistrationRepository) {
		this._bankRepository = bakBankRepository;
		this._collegeRegistrationRepository=collegeRegistrationRepository;
	}
	
	//save the Bank and College details
	//created on : 06/04/2026
	public BankDTO saveCollegeBank( BankDTO dto) {
		
		logger.info("@@@Calling the saveCollegeBank proc...................");
		
		// Fetch related entities
		
		College college =null;
		if (dto.getCollegeId() != null) {
			college = _collegeRegistrationRepository.findById(dto.getCollegeId())
					.orElseThrow(() -> new RuntimeException("College not found"));
		}
		
		// Convert DTO → Entity
		Bank bank = BankDTOMapper.toEntity(dto, college);
		
		// Set ID
		if (bank.getId() == null) { // New record
			bank.setId(Long.parseLong(DateUtil.getUniqueID()));
		}
		// set timestamps
		bank.setCreatedat(DateUtil.getCurrentDateTime());

		// 4️⃣ Save entity
		Bank savedBank = _bankRepository.save(bank);

		// 5️⃣ Convert back to DTO for response
		BankDTO responseDTO = BankDTOMapper.toDTO(savedBank);
		
		return responseDTO;
	}
	
	//get all the registered Banks with Colleges
	public List<BankDTO> getAllRegisteredBanks() {
		
		logger.info("@@@Calling this getAllRegisteredBanks proc...............");
		
		return _bankRepository.findAll().stream().map(BankDTOMapper::toDTO)
				.collect(Collectors.toList());
	}
}
