package com.sprms.system.master.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sprms.system.frmbeans.CollegeFrmBean;
import com.sprms.system.frmbeans.CollegesDTO;
import com.sprms.system.hbmbeans.Cities;
import com.sprms.system.hbmbeans.College;
import com.sprms.system.hbmbeans.Country;
import com.sprms.system.hbmbeans.Role;
import com.sprms.system.hbmbeans.State;
import com.sprms.system.master.dao.CityRepository;
import com.sprms.system.master.dao.CollegeRegistrationRepository;
import com.sprms.system.master.dao.CountryRepository;
import com.sprms.system.master.dao.StateRepository;
import com.sprms.system.modelMapper.CollegeFrmBeanMapper;
import com.sprms.system.modelMapper.CollegesDTOMapper;
import com.sprms.system.utils.DateUtil;
import com.sprms.system.wrapper.ServiceResponse;

@Service
public class CollegeRegistrationServices {

	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(CollegeRegistrationServices.class);

	// call the repository
	private final CollegeRegistrationRepository _collegeRegistrationRepository;
	private final CountryRepository _countryRepository;
	private final StateRepository _stateRepository;
	private final CityRepository _cityRepository;
	private final CollegesDTOMapper _collegesDTOMapper;

	// constructor
	public CollegeRegistrationServices(CollegeRegistrationRepository collegeRegistrationRepository,
			CountryRepository countryRepository, StateRepository stateRepository, CityRepository cityRepository,CollegesDTOMapper collegesDTOMapper) {
		this._collegeRegistrationRepository = collegeRegistrationRepository;
		this._countryRepository = countryRepository;
		this._stateRepository = stateRepository;
		this._cityRepository = cityRepository;
		this._collegesDTOMapper=collegesDTOMapper;

	}

	/**
	 * Save or update a College and return DTO with names populated
	 */
	public CollegesDTO saveCollege(CollegesDTO dto) {
		
		logger.info("@@@Calling the saveCollege save proc-------------");
		

		// 1️⃣ Fetch related entities
		Country country = null;
		if (dto.getCountryId() != null) {
			country = _countryRepository.findById(dto.getCountryId())
					.orElseThrow(() -> new RuntimeException("Country not found"));
		}

		State state = null;
		if (dto.getStateId() != null) {
			state = _stateRepository.findById(dto.getStateId())
					.orElseThrow(() -> new RuntimeException("State not found"));
		}

		Cities city = null;
		if (dto.getCityId() != null) {
			city = _cityRepository.findById(dto.getCityId()).orElseThrow(() -> new RuntimeException("City not found"));
		}

		// 2️⃣ Convert DTO → Entity
		College college = CollegesDTOMapper.toEntity(dto, country, state, city);

		// 3️⃣ Set timestamps
		if (college.getId() == null) { // New record
			college.setId(Long.parseLong(DateUtil.getUniqueID()));
		}
		college.setUpdatedDate(DateUtil.getCurrentDateTime());

		// 4️⃣ Save entity
		College savedCollege = _collegeRegistrationRepository.save(college);

		// 5️⃣ Convert back to DTO for response
		CollegesDTO responseDTO = CollegesDTOMapper.toDTO(savedCollege);

		return responseDTO;
	}

	// List all the colleges
	public List<College> getAllColleges() {

		logger.info("@@@calling list colleges proc------------");
		// get the colleges and map to frmbean and then return
		List<College> colleges = _collegeRegistrationRepository.findAll();

		// map to frmBean and then return
		// List<CollegeFrmBean> collegeFrmBean =
		// _collegeFrmBeanMapper.toFrmBean(colleges);

		return _collegeRegistrationRepository.findAll();
	}

	// get Colleges by its ID
	public College getCollegeById(Long id) {

		logger.info("@@@caliing the get college by id proc----------------");
		return _collegeRegistrationRepository.findById(id).orElse(null);
	}

	// delete college by its Id
	public void deleteCollegeById(Long id) {

		logger.info("@@@caliing the delete college proc---------------");
		_collegeRegistrationRepository.deleteById(id);
	}
}
