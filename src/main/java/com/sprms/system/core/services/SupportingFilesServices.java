package com.sprms.system.core.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sprms.system.core.servicesdao.SupportingFilesRepository;
import com.sprms.system.frmbeans.SupportingFilesDTO;
import com.sprms.system.hbmbeans.SupportingFiles;
import com.sprms.system.modelMapper.SupportingFilesDTOMapper;

@Service
public class SupportingFilesServices {

	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(SupportingFilesServices.class);

	// call the repository
	private final SupportingFilesRepository _supportingFilesRepository;

	// constructor
	public SupportingFilesServices(SupportingFilesRepository supportingFilesRepository) {
		this._supportingFilesRepository = supportingFilesRepository;
	}

	//Find the Supporting file by taking the application ID as parameter
	public List<SupportingFilesDTO> getFilesByApplicationId(Long applicationId) {

	    List<SupportingFiles> files =
	    		_supportingFilesRepository.findByScholarshipRegistrationId(applicationId);

	    return files.stream()
	            .map(SupportingFilesDTOMapper::toDTO)
	            .toList();
	}
}
