package com.sprms.system.master.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sprms.system.frmbeans.SchoolDTO;
import com.sprms.system.hbmbeans.Schools;
import com.sprms.system.master.services.SchoolsServices;
import com.sprms.system.modelMapper.SchoolDTOMapper;

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
	private final SchoolDTOMapper _schoolDTOMapper;

	// constructor
	public SchoolRegistrationController(SchoolsServices schoolsServices, SchoolDTOMapper schoolDTOMapper) {

		this._schoolsServices = schoolsServices;
		this._schoolDTOMapper = schoolDTOMapper;

	}

	// School Registration frm load
	@GetMapping("/registrationfrm")
	public String getSchoolRegistrationFrm(Model model) {

		logger.info("@@@Calling the school registration frm.................");

		model.addAttribute("schooldto", new SchoolDTO());

		return DISPLAY_SCHOOL_REGISTRATION_FRM;
		
	}

	// registere the new Higher Secondary School
	// created : 07/04/2026
	@PostMapping("/addschool")
	public String saveSchoolRegistration(@ModelAttribute SchoolDTO schoolDTO, Model model,
			RedirectAttributes redirectAttributes) {

		logger.info("@@@Calling the saveSchoolRegistration..............");
		try {

			// check and save the registrtaion
			_schoolsServices.saveSchool(schoolDTO);

			redirectAttributes.addFlashAttribute("message", "Information save successfully");

		} catch (Exception e) {
			// TODO: handle exception
			redirectAttributes.addFlashAttribute("message", "information could not be saved " + e.getMessage());
		}

		return "redirect:/school/registrationfrm";
		//return "school/registrationfrm :: content";
	}


	
	/*
	 * @PostMapping("/addschool")
	 * 
	 * @ResponseBody public Map<String, Object>
	 * saveSchoolRegistration(@ModelAttribute SchoolDTO schoolDTO) {
	 * 
	 * Map<String, Object> response = new HashMap<>();
	 * 
	 * try { _schoolsServices.saveSchool(schoolDTO);
	 * 
	 * response.put("status", "success"); response.put("message",
	 * "Information saved successfully"); response.put("url",
	 * "/school/registrationfrm");
	 * 
	 * } catch (Exception e) { response.put("status", "error");
	 * response.put("message", "Save failed: " + e.getMessage()); }
	 * 
	 * return response; }
	 */
	
	// edit the School Registration Data
	// created 07/04/2026
	@GetMapping("/registration/edit/{id}")
	public String editSchoolRegistration(@PathVariable Long id, Model model) {

		logger.info("@@@Calling editSchoolRegistration proc.............");
		try {

			// get the Registration detail by Id
			SchoolDTO schoolDTO = _schoolsServices.getSchoolById(id);

			model.addAttribute("schooldto", schoolDTO);

		} catch (Exception e) {
			// TODO: handle exception
			e.getMessage();
		}
		return DISPLAY_SCHOOL_REGISTRATION_FRM;
	}

	// delete the School registration
	// date 07/04/2026
	@GetMapping("/registration/delete/{id}")
	public String deleteSchool(@PathVariable Long id, RedirectAttributes redirectAttributes) {

		logger.info("@@@Calling the deleteSchool proc.....................");
		
		try {
			
			_schoolsServices.deleteSchoolById(id);
			redirectAttributes.addFlashAttribute("message", "School deleted successfully.");
		} catch (Exception e) {
			
			redirectAttributes.addFlashAttribute("message", "Delete failed: " + e.getMessage());
		}

		return "redirect:/school/lstschools";
	}

	// call the school list
	@GetMapping("/lstschools")
	public String getSchools(Model model, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		logger.info("@@@Calling the School list proc...................");

		Page<Schools> schools = _schoolsServices.getAllSchools(page, size);

		// Map entity page to FormBean page
		Page<SchoolDTO> schooPagedto = schools.map(_schoolDTOMapper::toDTO);

		model.addAttribute("schools", schooPagedto);

		return DISPLAY_SCHOOL_LIST;
	}

}
