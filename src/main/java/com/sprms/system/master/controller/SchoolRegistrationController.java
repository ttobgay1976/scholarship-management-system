package com.sprms.system.master.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sprms.system.frmbeans.SchoolFrmBean;
import com.sprms.system.hbmbeans.Schools;
import com.sprms.system.master.services.SchoolsServices;
import com.sprms.system.modelMapper.SchoolFrmBeanMapper;

@Controller
@RequestMapping("/school")
public class SchoolRegistrationController {

	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(SchoolRegistrationController.class);

	// setting the static variable for the form call
	private static String DISPLAY_COLLAGE_REGISTRATION_FRM = "CollegeRegistrationFrm";
	private static String DISPLAY_SCHOOL_LIST = "SchoolListFrm";
	private static String DISPLAY_SCHOOL_REGISTRATION_FRM = "SchoolRegistrationFrm";
	

	// call the service repository
	private final SchoolsServices _schoolsServices;
	private final SchoolFrmBeanMapper _schoolFrmBeanMapper;

	// constructor
	public SchoolRegistrationController(SchoolsServices schoolsServices, SchoolFrmBeanMapper schoolFrmBeanMapper) {

		this._schoolsServices = schoolsServices;
		this._schoolFrmBeanMapper = schoolFrmBeanMapper;

	}

	// School Registration frm load
	@GetMapping("/registrationfrm")
	public String getSchoolRegistrationFrm(Model model) {

		logger.info("@@@Calling the school registration frm.................");

		return DISPLAY_SCHOOL_REGISTRATION_FRM;
	}

	// call the school list
	@GetMapping("/lstschools")
	public String getSchools(Model model, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		logger.info("@@@Calling the School list proc...................");
		/*
		 * List<Schools> lstSchool = _schoolsServices.getAllSchools();
		 * 
		 * List<SchoolFrmBean> schoolfrmBean =
		 * _schoolFrmBeanMapper.toFrmBean(lstSchool); model.addAttribute("schools",
		 * schoolfrmBean);
		 */
		
        Page<Schools> collegePage = _schoolsServices.getAllSchools(page, size);
        
        // Map entity page to FormBean page
        Page<SchoolFrmBean> frmBeanPage = collegePage.map(_schoolFrmBeanMapper::toFrmBean);
        
		model.addAttribute("schools", frmBeanPage);
		
		return DISPLAY_SCHOOL_LIST;
	}
}
