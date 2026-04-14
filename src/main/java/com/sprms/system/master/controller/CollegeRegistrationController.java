package com.sprms.system.master.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sprms.system.frmbeans.CollegeFrmBean;
import com.sprms.system.frmbeans.CollegesDTO;
import com.sprms.system.frmbeans.CountryFrmBean;
import com.sprms.system.hbmbeans.College;
import com.sprms.system.hbmbeans.Country;
import com.sprms.system.master.services.CollegeRegistrationServices;
import com.sprms.system.master.services.CountryServices;
import com.sprms.system.modelMapper.CollegeFrmBeanMapper;
import com.sprms.system.modelMapper.CollegesDTOMapper;
import com.sprms.system.modelMapper.CountryFrmBeanMapper;
import com.sprms.system.wrapper.ServiceResponse;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/college")
public class CollegeRegistrationController {

	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(CollegeRegistrationController.class);

	// setting the static variable for the form call
	private static String DISPLAY_COLLAGE_REGISTRATION_FRM = "CollegeRegistrationFrm";
	private static String DISPLAY_COLLAGE_LIST = "CollegeListFrm";

//	declare the services
	private final CollegeRegistrationServices _collegeRegistrationServices;
	private final CollegesDTOMapper _collegesDTOMapper;
	private final CountryServices _countryServices;
	private final CountryFrmBeanMapper _countryFrmBeanMapper;

//	construction to initiaxze the variable
	public CollegeRegistrationController(CollegeRegistrationServices collegeRegistrationServices,
			CollegesDTOMapper collegesDTOMapper, CountryServices countryServices,
			CountryFrmBeanMapper countryFrmBeanMapper) {
		this._collegeRegistrationServices = collegeRegistrationServices;
		this._collegesDTOMapper = collegesDTOMapper;
		this._countryServices = countryServices;
		this._countryFrmBeanMapper = countryFrmBeanMapper;
	}

//	calling the Colleges ragistation from
	@GetMapping("/registrationfrm")
	public String getCollegeRegistrationfrm(Model model) {

		logger.info("@@@caliing the College Registration form........");
		model.addAttribute("collegefrmbean", new CollegeFrmBean());

		// get all the countries
		List<Country> lstCountry = _countryServices.getCountries();
		model.addAttribute("lstcountry", _countryFrmBeanMapper.toFrmBean(lstCountry));

		return DISPLAY_COLLAGE_REGISTRATION_FRM;
	}

//	College save method
	@PostMapping("/register")
	public String saveCollege(@Valid @ModelAttribute("college") CollegesDTO collegesDTO, BindingResult result,
			Model model, RedirectAttributes redirectAttributes) {

		logger.info("@@@Calling the college save method---------------");
		if (result.hasErrors()) {
			redirectAttributes.addFlashAttribute("message", "Information could not be saved");
			return "redirect:/college/registrationfrm";
		}

//		pass the value to save called method
//		call the service static method to get status
		CollegesDTO getResponse = _collegeRegistrationServices.saveCollege(collegesDTO);

		try {
			
//			this gets the response from the service layer and then redirect message form
			redirectAttributes.addFlashAttribute("message", "Information saved successfully");
		} catch (Exception e) {
			// TODO: handle exception
			redirectAttributes.addFlashAttribute("message", "Information not save: "+ e.getMessage());
		}

		return "redirect:/college/registrationfrm";
	}

	//get the college registration detial
	
	@GetMapping("/lstcolleges")
	public String getColleges(Model model) {

		logger.info("@@@Calling the college List------------------");
		List<College> lstCol = _collegeRegistrationServices.getAllColleges();

		for(College col :lstCol) {
			System.out.println("@@@Country Name :"+ col.getCountry().getCountryName());
		}
		model.addAttribute("colleges", lstCol);

		return DISPLAY_COLLAGE_LIST;
	}
	
	//edit the college registration Information
	//date : 08/04/2026
	@GetMapping("/registration/edit/{id}")
	public String editCollegeRegistration(@PathVariable Long id, Model model) {
		
		logger.info("@@@Calling the editCollegeRegistration....................");
		
		try {
			
			//get the college details
			CollegesDTO collegesDTO = _collegeRegistrationServices.getCollegeById(id);
			model.addAttribute("collegefrmbean", collegesDTO);
			
			// get all the countries
			List<Country> lstCountry = _countryServices.getCountries();
			model.addAttribute("lstcountry", _countryFrmBeanMapper.toFrmBean(lstCountry));
			
		} catch (Exception e) {
			// TODO: handle exception
			e.getMessage();
		}
		
		return DISPLAY_COLLAGE_REGISTRATION_FRM;
	}
	
	//delete the college from the list/tablr
	//created 08/04/2026
	@GetMapping("/registration/delete/{id}")
	public String deleteCollege(@PathVariable Long id, RedirectAttributes redirectAttributes) {

		logger.info("@@@Calling the deleteSchool proc.....................");
		
		try {
			
			//check and delete the registration from the data
			
			_collegeRegistrationServices.deleteCollegeById(id);
			
			redirectAttributes.addFlashAttribute("message", "College information deleted successfully.");
		} catch (Exception e) {
			
			redirectAttributes.addFlashAttribute("message", "Delete failed: " + e.getMessage());
		}

		return "redirect:/college/lstcolleges";
	}
	
}
