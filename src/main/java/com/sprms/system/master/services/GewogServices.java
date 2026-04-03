package com.sprms.system.master.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sprms.system.hbmbeans.GewogM;
import com.sprms.system.master.dao.GewogRepository;

@Service
public class GewogServices {

	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(GewogServices.class);
	
	//call reposiroty as a private
	private final GewogRepository _gewogRepository;
	
	//constructor to initialize the private variable
	public GewogServices(GewogRepository gewogRepository) {
		this._gewogRepository=gewogRepository;
	}
	
	//get the gewoglist
	public List<GewogM> findGewogByDzongkhagId(Long dzongkhagId){
		
		logger.info("@@@Calling the get gewog list proc-----------------");
		
		//get the list by calling the repository services
		List<GewogM> gewogs = _gewogRepository.findByDzongkhag_DzongkhagId(dzongkhagId);
		
		return gewogs;
	}
}
