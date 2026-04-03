package com.sprms.system.master.services;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sprms.system.frmbeans.StateDTO;
import com.sprms.system.hbmbeans.State;
import com.sprms.system.master.dao.StateRepository;

@Service
public class StateServices {

	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(StateServices.class);

	// call the repository and initiaze it with constructor
	private final StateRepository _stateRepository;

	// constructor
	public StateServices(StateRepository stateRepository) {
		this._stateRepository = stateRepository;
	}

	// get the state name by Country ID
	public List<State> findStateByCountryId(Long Id) {

		logger.info("@@@Calling the get state proc-----------");

		// get the state list and return the results
		List<State> states = _stateRepository.findByCountryId(Id);

		return states;
	}

	//New Pro usng DTO
	public List<StateDTO> getStatesByCountry(Long countryId) {

		return _stateRepository.findByCountryId(countryId).stream().map(s -> new StateDTO(s.getId(), s.getStateName()))
				.collect(Collectors.toList());

	}

}
