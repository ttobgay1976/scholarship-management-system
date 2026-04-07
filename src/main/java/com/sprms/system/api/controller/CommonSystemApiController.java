package com.sprms.system.api.controller;

import java.util.ArrayList;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.sprms.system.frmbeans.CitiesDTO;
import com.sprms.system.frmbeans.CollegesDTO;
import com.sprms.system.frmbeans.CustomUserDetails;
import com.sprms.system.frmbeans.MenuDTO;
import com.sprms.system.frmbeans.RoleMenuDTO;
import com.sprms.system.frmbeans.StateDTO;
import com.sprms.system.hbmbeans.Cities;
import com.sprms.system.hbmbeans.College;
import com.sprms.system.hbmbeans.GewogM;
import com.sprms.system.hbmbeans.Menu;
import com.sprms.system.hbmbeans.Role;
import com.sprms.system.hbmbeans.State;
import com.sprms.system.hbmbeans.User;
import com.sprms.system.hbmbeans.UserRoles;
import com.sprms.system.master.services.CityService;
import com.sprms.system.master.services.CollegeRegistrationServices;
import com.sprms.system.master.services.GewogServices;
import com.sprms.system.master.services.StateServices;
import com.sprms.system.modelMapper.CollegesDTOMapper;
import com.sprms.system.user.dao.MenuRepository;
import com.sprms.system.user.dao.RoleRepository;
import com.sprms.system.user.dao.UserRepository;
import com.sprms.system.user.dao.UserRoleRepository;
import com.sprms.system.user.services.MenuService;
import com.sprms.system.user.services.RoleMenuMapServices;
import com.sprms.system.user.services.RoleMenuService;
import com.sprms.system.user.services.UserRolesServices;

@RestController
@RequestMapping("/api")
public class CommonSystemApiController {

	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(CommonSystemApiController.class);

	// call the services
	private final StateServices _stateServices;
	private final GewogServices _gewogServices;
	private final CityService _cityService;
	private final CollegeRegistrationServices _collegeRegistrationServices;
	private final CollegesDTOMapper _collegesDTOMapper;
	private final MenuRepository _menuRepository;
	private final RoleMenuMapServices _roleMenuMapServices;
	private final RoleMenuService _roleMenuService;
	private final MenuService _menuService;
	private final UserRepository _userRepository;
	private final UserRolesServices _userRolesServices;

	// initialize the services
	public CommonSystemApiController(StateServices stateServices, GewogServices gewogServices, CityService cityService,
			CollegeRegistrationServices collegeRegistrationServices, CollegesDTOMapper collegesDTOMapper,
			RoleRepository roleRepository, MenuRepository menuRepository, RoleMenuMapServices roleMenuMapServices,
			RoleMenuService roleMenuService, MenuService menuService, UserRepository userRepository,UserRolesServices userRolesServices) {
		this._stateServices = stateServices;
		this._gewogServices = gewogServices;
		this._cityService = cityService;
		this._collegeRegistrationServices = collegeRegistrationServices;
		this._collegesDTOMapper = collegesDTOMapper;
		this._menuRepository = menuRepository;
		this._roleMenuMapServices = roleMenuMapServices;
		this._roleMenuService = roleMenuService;
		this._menuService = menuService;
		this._userRepository = userRepository;
		this._userRolesServices=userRolesServices;
	}

	@GetMapping("/states")
	public List<StateDTO> getStates(@RequestParam Long countryId) {

		logger.info("@@@Calling this getStates......................");
		System.out.println("@@@Country ID :" + countryId);

		return _stateServices.getStatesByCountry(countryId).stream().map(s -> new StateDTO(s.getId(), s.getStateName()))
				.collect(Collectors.toList());

	}

	@GetMapping("/cities")
	public List<CitiesDTO> getCities(@RequestParam("stateId") Long stateId) {

		System.err.println("@@@SateID passed:" + stateId);

		return _cityService.getCitiesByState(stateId);
	}

	// call the gewog by taking the dzongkhagid
	@GetMapping("/gewogs")
	public List<GewogM> getGewogs(@RequestParam Long dzongkhagId) {

		logger.info("@@@Calling the cities proc---------------");

		// get the gewog list
		List<GewogM> gewogs = _gewogServices.findGewogByDzongkhagId(dzongkhagId);

		return gewogs;
	}

	// colleges API which can be called by student registration module
	@GetMapping("/colleges")
	public List<CollegesDTO> getColleges() {

		logger.info("@@@Calling the College REST API by Student modules");
		List<College> colleges = _collegeRegistrationServices.getAllColleges();

		return _collegesDTOMapper.toDTOList(colleges);
	}

	// This REST API is prepare for the menu listing in Role Menu Mapping
	// This will List the parent and child menu with selectable option
	// created on dat 27/03/2026
	@GetMapping("/menu")
	public Map<String, Object> getMenusByRole(@RequestParam Long roleId) {

		logger.info("@@@Calling the API getMenusByRole.....................");

		// 2️⃣ Fetch assigned menus for this role
		Set<Menu> assignedMenus = _roleMenuMapServices.getMenusByRole(roleId);

		_menuRepository.findAll();

		// 3️⃣ Convert to DTO tree
//		List<MenuDTO> menuTree = buildMenuTree(allMenus);
		List<MenuDTO> menuTree = _menuService.getAllMenusTree();

		// 4️⃣ Prepare response
		Map<String, Object> response = new HashMap<>();
		response.put("menus", menuTree);
		response.put("assignedMenuIds", assignedMenus.stream().map(Menu::getId).collect(Collectors.toList()));

		return response;
	}

	// Helper to convert flat list to tree of DTOs
	private List<MenuDTO> buildMenuTree(List<Menu> menus) {
		Map<Long, MenuDTO> map = new HashMap<>();
		List<MenuDTO> roots = new ArrayList<>();

		// Convert all menus to DTOs
		for (Menu m : menus) {
			map.put(m.getId(),
					new MenuDTO(m.getId(), m.getMenuName(), m.getMenuUrl(), m.getIcon(), m.getDisplayOrder()));
		}

		// Build parent-child relationships
		for (Menu m : menus) {
			MenuDTO dto = map.get(m.getId());
			if (m.getParent() != null && map.containsKey(m.getParent().getId())) {
				map.get(m.getParent().getId()).getChildren().add(dto);
			} else {
				roots.add(dto); // top-level menu
			}
		}

		// Optional: sort by displayOrder
		sortMenuTree(roots);

		return roots;
	}

	// Helper to convert flat list to tree of DTOs
	private void sortMenuTree(List<MenuDTO> menuList) {
		menuList.sort(Comparator.comparingInt(m -> m.getDisplayOrder() != null ? m.getDisplayOrder() : 0));
		for (MenuDTO m : menuList) {
			sortMenuTree(m.getChildren());
		}
	}

	// This is Role Menu mapping save API
	// this will call this API and trigger the underlaying procedure and save the
	// mapping

	@PostMapping("/savemenu")
	public Map<String, String> saveRoleMenu(@RequestBody RoleMenuDTO dto) {

		logger.info("@@@Calling the saveRoleMenu proc.................");

		Long roleId = dto.getRoleId();
		List<Long> menuIds = dto.getMenuIds() != null ? dto.getMenuIds() : new ArrayList<>();

		_roleMenuService.updateRoleMenus(roleId, menuIds);

		return Map.of("message", "Saved successfully");
	}

	// this API will get user menu and will display in the sidebar menu lis

	@GetMapping("/menus")
	public ResponseEntity<?> getUserMenus() {

		logger.info("@@@Calling the menu rest API getUserMenus.......... ");

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName(); // logged-in username

		User user = _userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
		System.out.println("@@@First Name :" + user.getFirstname());
		System.out.println("@@@ User ID:" + user.getId());

		Long userId = user.getId();

		System.out.println("@@@Checking the user ID:" + userId);

		List<MenuDTO> menus = _menuService.getMenusByUserId(userId);
		return ResponseEntity.ok(menus);
	}

	// get the assigned role to user and checkbox get checked	
	// .../api/user/20260402035126/roles -->pass like this in postman test
	@GetMapping("/user/{userId}/roles")
	@ResponseBody
	public List<Long> getUserRoles(@PathVariable Long userId) {
		
		logger.info("@@@Calling theis proc getUserRoles....................");
		List<Long> lstRoles = _userRolesServices.getAssignedRoleIds(userId);
		
		for(Long lo :lstRoles) {
			System.out.println("@@Value of Roles:"+ lo);
		}
	    return lstRoles;
	}
	

	// COUNTRY REST API
	// THIS WILL GIVE LIST OF COUNTRIES TO THE COLLAR
	// DATE: 02/04/2026

}
