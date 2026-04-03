package com.sprms.system.master.services;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sprms.system.frmbeans.CitiesDTO;
import com.sprms.system.hbmbeans.Cities;
import com.sprms.system.master.dao.CityRepository;

@Service
public class CityService {
	
	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(CityService.class);
	
	//call the repository
	private final CityRepository _cityRepository;
	
	//constructor
	public CityService(CityRepository citiCityRepository) {
		this._cityRepository=citiCityRepository;
	}
	
	//get the cities by StateId
    public List<Cities> getCitiesByStateId(Long Id) {
        return _cityRepository.findByStateId(Id);
    }

    //new using DTO
    
    public List<CitiesDTO> getCitiesByState(Long stateId) {
    	
        return _cityRepository.findByStateId(stateId)
                .stream()
                .map(c -> new CitiesDTO(c.getId(), c.getCityName()))
                .collect(Collectors.toList());
    }
}
