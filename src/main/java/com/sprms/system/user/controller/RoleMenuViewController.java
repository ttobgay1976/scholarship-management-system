package com.sprms.system.user.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sprms.system.frmbeans.MenuDTO;
import com.sprms.system.frmbeans.RoleMenuMapFrmBean;
import com.sprms.system.hbmbeans.Menu;
import com.sprms.system.hbmbeans.Role;
import com.sprms.system.user.dao.MenuRepository;
import com.sprms.system.user.dao.RoleRepository;
import com.sprms.system.user.services.RoleMenuMapServices;
import com.sprms.system.user.services.RoleMenuService;
import com.sprms.system.user.services.RoleServices;

@Controller
@RequestMapping("/menu_NO_MORE_USE")
public class RoleMenuViewController {

	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(RoleMenuViewController.class);

	// call the servies
	private final MenuRepository _menuRepository;
	private final RoleMenuService _roleMenuService;
	private final RoleServices _roleServices;
	private final RoleMenuMapServices _roleMenuMapServices;

	// setting the static variable for the form call
	private static String DISPLAY_ROLE_MENU_MAPPING_FRM = "RoleMenuMappFrm";

	// constructor
	public RoleMenuViewController(MenuRepository menuRepository, RoleMenuService roleMenuService,
			RoleRepository roleRepository, RoleServices roleServices, RoleMenuMapServices roleMenuMapServices) {
		this._menuRepository = menuRepository;
		this._roleMenuService = roleMenuService;
		this._roleServices = roleServices;
		this._roleMenuMapServices=roleMenuMapServices;
	}

	// Load page
	@GetMapping("/rolemenumapfrm")
	public String loadRoleMenuPage(Model model) {

		logger.info("@@@Calling the loadRoleMenuPage proc.......................... ");

		List<Menu> lstMenus = _menuRepository.findAll();
		for(Menu mnu: lstMenus) {
			System.out.println("@@@Checking the return menus:"+ mnu.getMenuName());
			System.out.println("@@@Checking the return menus Url:"+ mnu.getMenuUrl());
		}
		model.addAttribute("menus", lstMenus);

		List<Role> lstAllRoles = _roleServices.getRoles();
		model.addAttribute("roles", lstAllRoles);

		// form bean
		model.addAttribute("rolemenufrmbean", new RoleMenuMapFrmBean());

		return DISPLAY_ROLE_MENU_MAPPING_FRM;
	}

	// Save mapping
	@PostMapping("/save_Old")
	public String saveRoleMenu_Old(@RequestParam Long roleId, @RequestParam(required = false) List<Long> menuIds) {

		_roleMenuService.updateRoleMenus(roleId, menuIds != null ? menuIds : new ArrayList<>());

		return "redirect:/role-menu/" + roleId;
	}

	// Load menus for selected role (AJAX GET)
	@GetMapping("/menus")
	@ResponseBody
	public Map<String, Object> getMenusForRole(@RequestParam Long roleId) {

		Role role = _roleServices.getRoleById(roleId);
		List<Menu> allMenus = _menuRepository.findAll();
		Set<Menu> assignedMenus = _roleMenuMapServices.getMenusByRole(roleId);

		Map<String, Object> response = new HashMap<>();
		response.put("menus", buildMenuTree(allMenus));
		response.put("assignedMenuIds", assignedMenus.stream().map(Menu::getId).toList());
		response.put("roleName", role.getRoleName());

		return response;
	}

	// Save menu assignments (AJAX POST)
	@PostMapping("/save")
	@ResponseBody
	public Map<String, Object> saveRoleMenu(@RequestParam Long roleId, @RequestBody List<Long> menuIds) {
		_roleMenuService.updateRoleMenus(roleId, menuIds);
		Map<String, Object> response = new HashMap<>();
		response.put("status", "success");

		return response;
	}

	// Build tree
	private List<Menu> buildMenuTree_Old(List<Menu> menus) {
		Map<Long, Menu> map = menus.stream().collect(Collectors.toMap(Menu::getId, m -> m));

		List<Menu> roots = new ArrayList<>();
		for (Menu menu : menus) {
			if (menu.getParent() != null && map.containsKey(menu.getParent().getId())) {
				map.get(menu.getParent().getId()).getChildren().add(menu);
			} else {
				roots.add(menu);
			}
		}
		return roots;
	}


	// loading the menu dinamically by the ajax call
	public static List<MenuDTO> buildMenuTree(List<Menu> menus) {


		logger.info("@@@Calling the menu load by Ajax..................");
		
		Map<Long, MenuDTO> map = new HashMap<>();
		List<MenuDTO> roots = new ArrayList<>();

		// Step 1: Convert all menus to DTO
		for (Menu m : menus) {
			map.put(m.getId(),
					new MenuDTO(m.getId(), m.getMenuName(), m.getMenuUrl(), m.getIcon(), m.getDisplayOrder()));
		}

		// Step 2: Link children to parents
		for (Menu m : menus) {
			MenuDTO dto = map.get(m.getId());
			if (m.getParent() != null && map.containsKey(m.getParent().getId())) {
				map.get(m.getParent().getId()).getChildren().add(dto);
			} else {
				roots.add(dto); // top-level menu
			}
		}

		// Optional: sort children by displayOrder
		sortMenuTree(roots);

		return roots;
	}

	private static void sortMenuTree(List<MenuDTO> menuList) {
		menuList.sort(Comparator.comparingInt(m -> m.getDisplayOrder() != null ? m.getDisplayOrder() : 0));
		for (MenuDTO m : menuList) {
			sortMenuTree(m.getChildren());
		}
	}
}
