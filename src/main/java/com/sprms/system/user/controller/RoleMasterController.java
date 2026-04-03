package com.sprms.system.user.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sprms.system.frmbeans.RoleFrmBean;
import com.sprms.system.frmbeans.UserFormBean;
import com.sprms.system.frmbeans.UserRolesFrmBean;
import com.sprms.system.hbmbeans.Role;
import com.sprms.system.hbmbeans.User;
import com.sprms.system.modelMapper.RolesBeanMapper;
import com.sprms.system.modelMapper.UserFrmBeanMapper;
import com.sprms.system.modelMapper.UserRolesBeanMapper;
import com.sprms.system.user.services.RoleServices;
import com.sprms.system.user.services.UserRolesServices;
import com.sprms.system.user.services.UserService;
import com.sprms.system.utils.DateUtil;
import com.sprms.system.wrapper.ServiceResponse;

@Controller
@RequestMapping("/role")
public class RoleMasterController {

	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(RoleMasterController.class);

private static String DISPLAY_ROLE_MANAGEMENT_FRM = "RoleMasterFrm";
	private static String DISPLY_LIST_ROLES ="ListRolesFrm";
	
//	call the repository

	private final RoleServices roleServices;
	private final RolesBeanMapper rolesBeanMapper;
	public RoleMasterController(UserService userService, RoleServices roleServices, RolesBeanMapper rolesBeanMapper,
			UserRolesBeanMapper userRolesBeanMapper, UserRolesServices userRolesServices,UserFrmBeanMapper userFrmBeanMapper) {
		this.roleServices = roleServices;
		this.rolesBeanMapper = rolesBeanMapper;
	}
	
	// Role management
	@GetMapping("/addrolefrm")
	public String addRole(Model model) {

		// add addtribute to form
		model.addAttribute("role", new RoleFrmBean());

		return DISPLAY_ROLE_MANAGEMENT_FRM;

	}

	// post role
	@PostMapping("/addrole")
	public String addRole(@ModelAttribute("role") RoleFrmBean roleFrmBean, RedirectAttributes redirectAttributes) {

		logger.info("@@@Calling th role save procedure---------------");

		// invoke ServiceResponse
		Role role = new Role();
		role = rolesBeanMapper.toEntity(roleFrmBean);

		role.setRoleId(Long.parseLong(DateUtil.getUniqueID()));

		ServiceResponse<Role> getResponse = roleServices.saveRole(role);

		// check the returntype
		if (getResponse.isSuccess()) {
			redirectAttributes.addFlashAttribute("message", getResponse.getMessage());
		} else {
			redirectAttributes.addFlashAttribute("message", getResponse.getMessage());
		}

		return "redirect:/role/addrolefrm";
	}
	
//	List the roles
	@GetMapping("/lstroles")
	public String getRoles(Model model) {
		
		logger.info("@@@Calling the list Users Rols pro------------");
		
		List<Role> listRoles = roleServices.getRoles();
		model.addAttribute("lstRoles", listRoles);
		
		return DISPLY_LIST_ROLES;
		
	}
	
}
