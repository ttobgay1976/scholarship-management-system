package com.sprms.system.user.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sprms.system.frmbeans.RoleFrmBean;
import com.sprms.system.frmbeans.UserFormBean;
import com.sprms.system.frmbeans.UserRolesFrmBean;
import com.sprms.system.hbmbeans.Role;
import com.sprms.system.hbmbeans.User;
import com.sprms.system.hbmbeans.UserRoles;
import com.sprms.system.modelMapper.RolesBeanMapper;
import com.sprms.system.modelMapper.UserFrmBeanMapper;
import com.sprms.system.user.services.RoleServices;
import com.sprms.system.user.services.UserRolesServices;
import com.sprms.system.user.services.UserService;

@Controller
@RequestMapping("/user")
public class UserRegistrationController {

	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(UserRegistrationController.class);

	// setting the static variable for the form call
	private static String DISPLAY_ADMIN_USER_REGISTRATION_FRM = "AdminUserRegistrationFrm";
	private static String DISPLAY_USERs = "ListUserFrm";
	private static String DISPLAY_USER_ROLE_MAPPING_FRM = "userrolemappingfrm";

//		calling the repository
	private final UserService _userService;
	private final UserRolesServices _userRolesServices;
	private final RoleServices _roleServices;
	private final UserFrmBeanMapper _userFrmBeanMapper;
	private final RolesBeanMapper _rolesBeanMapper;
	

	public UserRegistrationController(UserService userService, UserRolesServices userRolesServices,
			RoleServices roleServices,UserFrmBeanMapper userFrmBeanMapper,RolesBeanMapper rolesBeanMapper) {
		this._userService = userService;
		this._userRolesServices = userRolesServices;
		this._roleServices = roleServices;
		this._userFrmBeanMapper=userFrmBeanMapper;
		this._rolesBeanMapper=rolesBeanMapper;
	}

	@GetMapping("/registrationfrm")
	public String getUserRegistrationForm(Model model) {

		logger.info("@@@Calling the User Registration Form--------");
		model.addAttribute("newuser", new UserFormBean());

		return DISPLAY_ADMIN_USER_REGISTRATION_FRM;
	}

	@PostMapping("/saveuser")
	public String addUser(@ModelAttribute("newuser") UserFormBean userRegistrationFrmBean,
			RedirectAttributes redirectAttributes) {

		logger.info("@@@Calling the new User save method---------------------");

		System.out.println("@@@Pass:" + userRegistrationFrmBean.getPassword());
		System.out.println("@@@Confrim:" + userRegistrationFrmBean.getConfirmpwd());

//			checking the password and passwordconfirmation
		if (!userRegistrationFrmBean.getPassword().equals(userRegistrationFrmBean.getConfirmpwd())) {
			redirectAttributes.addFlashAttribute("message", "Password does not match, try again");
			return "redirect:/user/registrationfrm";
		}

//			password matches then precess further
		User userEntity = new User();

//			setting and mapping form bean to java entity
		userEntity.setFirstname(userRegistrationFrmBean.getFirstname());
		userEntity.setLastname(userRegistrationFrmBean.getLastname());
		userEntity.setCidno(userRegistrationFrmBean.getCidno());
		userEntity.setContactno(userRegistrationFrmBean.getContactno());
		userEntity.setPassword(userRegistrationFrmBean.getPassword());
		userEntity.setUsername(userRegistrationFrmBean.getUsername());

		try {
			_userService.registerNewUser(userEntity);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		redirectAttributes.addFlashAttribute("message", "User Added Successfully");
		return "redirect:/user/registrationfrm";
	}

//		list the users
	@GetMapping("/userlists")
	public String listUsers(Model model) {

		logger.info("@@@Calling the user list procedure..............");

		List<User> users = _userService.listUsers();
		model.addAttribute("users", users);

		return DISPLAY_USERs;
	}

//		for the testing
	@RequestMapping("/others")
	public String getOtherPage() {
		return "OtherFrm";
	}

//	User Roles Mapping
//	created on date 22/03/2026
//	Author : TTobgay
//	Home
	@GetMapping("/userrolemapfrm")
	public String getUserRoleMappingFrm(Model model) {

		logger.info("@@@Calling the User and Role mapping Pro-------------");

//		setting for the form load
		UserRolesFrmBean userRolesFrmBean = new UserRolesFrmBean();
		model.addAttribute("userrolemap", userRolesFrmBean);

//		listing the Roles and map to formBean with mappers
		List<Role> lstRoles = _roleServices.getRoles();
		List<RoleFrmBean> roletofrmbean = _rolesBeanMapper.toFrmBean(lstRoles);
		model.addAttribute("roles", roletofrmbean);

//		list the users and map to from bean with mapper
		List<User> lstUser = _userService.listUsers();
		List<UserFormBean> usertofrmbean = _userFrmBeanMapper.toFrmBean(lstUser);
		
		model.addAttribute("users", usertofrmbean);

		return DISPLAY_USER_ROLE_MAPPING_FRM;
	}

	// save User Role map
	// there will be more than one Role selected for the one user
	//this is fpr the API call
	@PostMapping("/userrolemap_Old")
	public ResponseEntity<List<UserRoles>> assignMultipleRoles_API(@RequestParam("id") Long userId,
			@RequestParam List<Long> roleIds) {

		logger.info("@@@Calling assignMultipleRoles porc................. ");

		List<UserRoles> assignedRoles = _userRolesServices.assignRolesToUser(userId, roleIds);
		return ResponseEntity.ok(assignedRoles);
	}
	
	// save User Role map
	// there will be more than one Role selected for the one user
	@PostMapping("/userrolemap")
	public String assignMultipleRoles(
	        @RequestParam("id") Long userId,
	        @RequestParam List<Long> roleIds,
	        RedirectAttributes redirectAttributes) {

	    try {
	        _userRolesServices.assignRolesToUser(userId, roleIds);

	        // Add success message
	        redirectAttributes.addFlashAttribute("message",
	                "Roles assigned successfully to user: " + userId);

	    } catch (Exception e) {
	        e.printStackTrace();

	        // Add error message
	        redirectAttributes.addFlashAttribute("message",
	                "Failed to assign roles: " + e.getMessage());
	    }

	    // Redirect back to the form page
	    return "redirect:/user/userrolemapfrm";
	}

}
