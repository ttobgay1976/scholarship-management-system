package com.sprms.advicer.controller;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.sprms.system.frmbeans.MenuDTO;
import com.sprms.system.hbmbeans.Menu;
import com.sprms.system.hbmbeans.User;
import com.sprms.system.user.dao.UserRepository;
import com.sprms.system.user.dao.UserRoleRepository;
import com.sprms.system.user.services.MenuService;

@ControllerAdvice
public class GlobalControllerAdvice {

	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(GlobalControllerAdvice.class);
	
	@Autowired
	private MenuService menuService;

	@Autowired
	private UserRepository userRepository;
	
	@ModelAttribute("menus")
	public List<MenuDTO> populateMenus(Authentication authentication) {

		logger.info("@@@Calling the GlobalControllerAdvice proc..........");

		if (authentication == null || authentication.getName().equals("anonymousUser")) {
			return Collections.emptyList();
		}

		String username = authentication.getName();
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("User not found"));
		    
		List<MenuDTO> menus = menuService.getSidebarMenus(user.getId());
		
        // 🔹 DEBUG LOG
		/* System.out.println("Logged-in user: " + username + ", menus: " + menus); */
        
        return menus;
	}
	
}
