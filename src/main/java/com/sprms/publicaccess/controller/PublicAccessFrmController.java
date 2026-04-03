package com.sprms.publicaccess.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sprms.system.frmbeans.UserFormBean;


@Controller
@RequestMapping("/public")
public class PublicAccessFrmController {
	
	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(PublicAccessFrmController.class);
	
	// setting the static variable for the form call
	private static String DISPLAY_USER_REGISTRATION_FRM = "UserRegistrationFrm";
	private static String DISPLAY_STUDENT_REGISTRATION_FRM = "StudentRegistrationFrm";
	
	//call the user registration frm
	@GetMapping("/userregistrstionfrm")
	public String getUserRegistrationFrm(Model model) {
		
		logger.info("@@@Calling the User Registration from by public.........");
		model.addAttribute("newuser", new UserFormBean());
		
		return DISPLAY_USER_REGISTRATION_FRM;
	}
	
	//get the Student Registration frm
	@GetMapping("/studentregistrationfrm")
	public String getStudentRegistrationFrm(Model model) {
		
		logger.info("@@@Calling the Student Registration frm...............");
		
		model.addAttribute("student",null);
		
		return DISPLAY_STUDENT_REGISTRATION_FRM;
	}

}
