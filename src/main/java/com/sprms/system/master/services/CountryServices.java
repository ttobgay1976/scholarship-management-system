package com.sprms.system.master.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sprms.system.hbmbeans.Country;
import com.sprms.system.master.dao.CountryRepository;

@Service
public class CountryServices {

	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(CountryServices.class);

	//call the repository for intialization
	private final CountryRepository _countryRepository;
	
	//constructor
	public CountryServices(CountryRepository countryRepository) {
		this._countryRepository=countryRepository;
	}
	
	//list the countries
	public List<Country> getCountries(){
		
		logger.info("@@@Calling the country list proc--------------");
		
		//this return all the countries to caller
		return _countryRepository.findAll();
	}
}
