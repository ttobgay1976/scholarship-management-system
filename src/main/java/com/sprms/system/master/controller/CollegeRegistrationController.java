package com.sprms.system.master.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sprms.system.frmbeans.CollegeFrmBean;
import com.sprms.system.frmbeans.CountryFrmBean;
import com.sprms.system.hbmbeans.College;
import com.sprms.system.hbmbeans.Country;
import com.sprms.system.master.services.CollegeRegistrationServices;
import com.sprms.system.master.services.CountryServices;
import com.sprms.system.modelMapper.CollegeFrmBeanMapper;
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
	private final CollegeFrmBeanMapper _collegeFrmBeanMapper;
	private final CountryServices _countryServices;
	private final CountryFrmBeanMapper _countryFrmBeanMapper;

//	construction to initiaxze the variable
	public CollegeRegistrationController(CollegeRegistrationServices collegeRegistrationServices,
			CollegeFrmBeanMapper collegeFrmBeanMapper, CountryServices countryServices,
			CountryFrmBeanMapper countryFrmBeanMapper) {
		this._collegeRegistrationServices = collegeRegistrationServices;
		this._collegeFrmBeanMapper = collegeFrmBeanMapper;
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
	public String saveCollege(@Valid @ModelAttribute("college") CollegeFrmBean collegeFrmBean, BindingResult result,
			Model model, RedirectAttributes redirectAttributes) {

		logger.info("@@@Calling the college save method---------------");
		if (result.hasErrors()) {
			redirectAttributes.addFlashAttribute("message", "Information could not be saved");
			return "redirect:/college/registrationfrm";
		}

//		use mapper to map the frmBean to entity bean
		College college = new College();
		college = _collegeFrmBeanMapper.toEntity(collegeFrmBean);

//		pass the value to save called method
//		call the service static method to get status
		ServiceResponse<College> getResponse = _collegeRegistrationServices.saveCollege(college);

//		this gets the response from the service layer and then redirect message form
		redirectAttributes.addFlashAttribute("message", getResponse.getMessage());

		return "redirect:/college/registrationfrm";
	}

	@GetMapping("/lstcolleges")
	public String getColleges(Model model) {

		logger.info("@@@Calling the college List------------------");
		List<College> lstCol = _collegeRegistrationServices.getAllColleges();
		for(College col :lstCol) {
			System.out.println("@@@Coll Name :"+ col.getCollegeName());
			System.out.println("@@@Coll Name :"+ col.getCity().getCityName());
		}
		
		model.addAttribute("colleges", _collegeRegistrationServices.getAllColleges());

		return DISPLAY_COLLAGE_LIST;
	}
}
