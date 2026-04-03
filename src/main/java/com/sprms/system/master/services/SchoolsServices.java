package com.sprms.system.master.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.sprms.system.hbmbeans.Schools;
import com.sprms.system.master.dao.SchoolsRepository;

@Service
public class SchoolsServices {

	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(SchoolsServices.class);

	// call repository
	private final SchoolsRepository _schoolsRepository;

	// constructor to initialize the repo
	public SchoolsServices(SchoolsRepository schoolsRepository) {
		this._schoolsRepository = schoolsRepository;
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

}
