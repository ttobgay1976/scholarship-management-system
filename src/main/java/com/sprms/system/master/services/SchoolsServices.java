package com.sprms.system.master.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.sprms.system.frmbeans.SchoolDTO;
import com.sprms.system.hbmbeans.Schools;
import com.sprms.system.master.dao.SchoolsRepository;
import com.sprms.system.modelMapper.SchoolDTOMapper;
import com.sprms.system.utils.DateUtil;

@Service
public class SchoolsServices {

	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(SchoolsServices.class);

	// call repository
	private final SchoolsRepository _schoolsRepository;
	private final SchoolDTOMapper _schoolDTOMapper;

	// constructor to initialize the repo
	public SchoolsServices(SchoolsRepository schoolsRepository, SchoolDTOMapper schoolDTOMapper) {
		this._schoolsRepository = schoolsRepository;
		this._schoolDTOMapper = schoolDTOMapper;
	}

	// list the school
	public List<Schools> getAllSchools() {

		logger.info("@@@Calling the get school proc----------------");

		// get all the school and return to the caller
		List<Schools> schools = _schoolsRepository.findAll();

		return schools;
	}

	// get the school list with pageination
	public Page<Schools> getAllSchools(int page, int size) {

		logger.info("@@@Calling the School list the Pagination..............");

		return _schoolsRepository.findAll(PageRequest.of(page, size));
	}

	// modified procedure to check edited data and new insertion
	// date 07/04/2026
	public SchoolDTO saveSchool(SchoolDTO dto) {

		logger.info("@@@Calling the saveSchool proc...............");

		Schools entity;

		if (dto.getId() != null) {
			// EDIT (update existing)
			entity = _schoolsRepository.findById(dto.getId())
					.orElseThrow(() -> new RuntimeException("School not found with id: " + dto.getId()));

			// update fields
			entity.setSchoolName(dto.getSchoolName());
			entity.setSchoolLocation(dto.getSchoolLocation());
			entity.setSchoolCode(dto.getSchoolCode());
			entity.setSchoolType(dto.getSchoolType());
			entity.setUpdateat(DateUtil.getCurrentDateTime());

		} else {
			// NEW INSERT
			entity = _schoolDTOMapper.toEntity(dto);

			entity.setId(Long.parseLong(DateUtil.getUniqueID()));
			entity.setCreatedat(DateUtil.getCurrentDateTime());
		}

		// save (works for both insert & update)
		Schools saved = _schoolsRepository.save(entity);

		return _schoolDTOMapper.toDTO(saved);
	}

	//delete the Registrtaion
	//date 07/04/2026
	public void deleteSchoolById(Long id) {

	    logger.info("@@@Calling deleteSchoolById proc...............");

	    Schools entity = _schoolsRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("School not found with id: " + id));

	    _schoolsRepository.delete(entity);
	}
	
	// edit the School Registration by ID
	public SchoolDTO getSchoolById(Long id) {

		logger.info("@@@Calling getSchoolById proc...............");

		Schools entity = _schoolsRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("School not found with id: " + id));

		return _schoolDTOMapper.toDTO(entity);
	}

}
