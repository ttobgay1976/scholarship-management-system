package com.sprms.home.controller;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.config.authentication.UserServiceBeanDefinitionParser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sprms.system.frmbeans.MenuDTO;
import com.sprms.system.hbmbeans.Menu;
import com.sprms.system.hbmbeans.User;
import com.sprms.system.user.services.MenuService;
import com.sprms.system.user.services.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class HomePageController {

	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(HomePageController.class);

	// setting the static variable for the form call
	private static String DISPLAY_HOMEPAGE = "HomeFrm";
	private static String DISPLAY_LOGIN_PAGE = "LoginFrm";
	private static String DISPLAY_USER_DASHBOARD_PAGE = "layouts/dashboard";

	// call services
	private final MenuService _menuService;
	private final UserService _userService;

	// constructor
	public HomePageController(MenuService menuService, UserService userService) {
		this._menuService = menuService;
		this._userService = userService;
	}

	@RequestMapping("/")
	public String getHomepge(Model map) {

		logger.info("@@@Calling the homePage controller");

		return DISPLAY_HOMEPAGE;

	}

	@RequestMapping("/login")
	public String getLoginPage(Model model) throws Exception {

		logger.info("@@@Calling the login Page");

		model.addAttribute("user", new User());
		return DISPLAY_LOGIN_PAGE;
	}

//	calling the dashboard after validating the user credential
	@GetMapping("/dashboard")
	public String getDashboard(Model model, Principal principal, HttpServletRequest request) {

		logger.info("@@@@Calling the getDashboard proc...................");

		String username = principal.getName();

		User user = _userService.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("User not found: " + username));

		// Add username or full user object to model
		model.addAttribute("username", user.getUsername());

		/* List<MenuDTO> menus = _menuService.getSidebarMenus(user.getId()); */
		// Debug
		// System.out.println("@@@ sidebarMenus size: " + (sidebarMenus != null ?
		// sidebarMenus.size() : "NULL"));
		
		return DISPLAY_USER_DASHBOARD_PAGE;
	}

	// Flatten tree to stream
	private Stream<MenuDTO> flatten(MenuDTO menu) {
		Stream<MenuDTO> self = Stream.of(menu);
		Stream<MenuDTO> children = (menu.getChildren() != null)
				? menu.getChildren().stream().flatMap(child -> flatten(child))
				: Stream.empty();
		return Stream.concat(self, children);
	}

}
