package com.sprms.system.master.services;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
	public BankServices(BankRepository bakBankRepository, CollegeRegistrationRepository collegeRegistrationRepository) {
		this._bankRepository = bakBankRepository;
		this._collegeRegistrationRepository = collegeRegistrationRepository;
	}

	// new produre to save Edited data and new data insertion from the same rendered
	// form
	// dated 07/04/2026
	public BankDTO saveCollegeBank(BankDTO bankDTO) {

		logger.info("@@@Calling the saveCollegeBank proc...................");

		College college = null;
		if (bankDTO.getCollegeId() != null) {
			college = _collegeRegistrationRepository.findById(bankDTO.getCollegeId())
					.orElseThrow(() -> new RuntimeException("College not found"));
		}

		Bank entity;
		if (bankDTO.getId() != null) {

			// Edit existing
			entity = _bankRepository.findById(bankDTO.getId())
					.orElseThrow(() -> new RuntimeException("Bank not found with id: " + bankDTO.getId()));

			entity.setCollege(college);
			entity.setAccountHolderName(bankDTO.getAccountHolderName());
			entity.setBankName(bankDTO.getBankName());
			entity.setBranchName(bankDTO.getBranchName());
			entity.setAccountNo(bankDTO.getAccountNo());
			entity.setIfscSwiftCode(bankDTO.getIfscSwiftCode());
			entity.setUpdatedat(DateUtil.getCurrentDateTime());

		} else {
			// New insert
			entity = BankDTOMapper.toEntity(bankDTO, college);
			entity.setId(Long.parseLong(DateUtil.getUniqueID()));
			entity.setCreatedat(DateUtil.getCurrentDateTime());
		}

		entity = _bankRepository.save(entity);

		return BankDTOMapper.toDTO(entity);
	}

	// get all the registered Banks with Colleges
	public List<BankDTO> getAllRegisteredBanks() {

		logger.info("@@@Calling this getAllRegisteredBanks proc...............");

		return _bankRepository.findAll().stream().map(BankDTOMapper::toDTO).collect(Collectors.toList());
	}

	//get Bank by Id
	public BankDTO getByID(Long id) {

		logger.info("@@@Calling getByID for Bank...............");

		Bank entity = _bankRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Bank not found with id: " + id));

		return BankDTOMapper.toDTO(entity);
	}

	//delete the Bank Registration
	public void deleteBankById(Long id) {
		
		logger.info("@@@Calling the deleteBankById.....................");
		
	    Bank entity = _bankRepository.findById(id)
	        .orElseThrow(() -> new RuntimeException("Not found"));
	    _bankRepository.delete(entity);
	}
}
