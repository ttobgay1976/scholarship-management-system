package com.sprms.system.user.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.sprms.system.frmbeans.MenuDTO;
import com.sprms.system.hbmbeans.Menu;
import com.sprms.system.hbmbeans.Role;
import com.sprms.system.hbmbeans.RoleMenuMap;
import com.sprms.system.hbmbeans.User;
import com.sprms.system.hbmbeans.UserRoles;
import com.sprms.system.user.dao.MenuRepository;
import com.sprms.system.user.dao.RoleMenuMapRepository;
import com.sprms.system.user.dao.UserRepository;
import com.sprms.system.user.dao.UserRoleRepository;
import com.sprms.system.utils.DateUtil;
import com.sprms.system.wrapper.ServiceResponse;

import jakarta.transaction.Transactional;

@Service
public class MenuServiceImpl implements MenuService {

	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(MenuServiceImpl.class);

	private final MenuRepository _menuRepository;
	private final UserRepository _userRepository;
	private final RoleMenuMapRepository _roleMenuMapRepository;
	private final UserRoleRepository _userRoleRepository;

	public MenuServiceImpl(MenuRepository menuRepository, UserRepository userRepository,
			RoleMenuMapRepository roleMenuMapRepository, UserRoleRepository userRoleRepository) {
		this._menuRepository = menuRepository;
		this._userRepository = userRepository;
		this._roleMenuMapRepository = roleMenuMapRepository;
		this._userRoleRepository = userRoleRepository;
	}

	@Override
	public Menu saveMenu(Menu menu) {
		return _menuRepository.save(menu);
	}

	@Override
	public List<Menu> getAllMenus() {
		return _menuRepository.findAll();
	}

	@Override
	public Menu getMenuById(Long id) {
		return _menuRepository.findById(id).orElseThrow(() -> new RuntimeException("Menu not found"));
	}

	@Override
	public void deleteMenu(Long id) {
		_menuRepository.deleteById(id);
	}

	// 🔥 Dynamic menu based on user roles
	@Override
	public Set<Menu> getMenusByUser(Long userId) {

		User user = _userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

		Set<Menu> menus = new HashSet<>();

		for (UserRoles ur : user.getUserRoles()) {
			Role role = ur.getRole();
			menus.addAll(role.getMenus());
		}

		return menus;
	}

	@Override
	public ServiceResponse<Menu> saveMenus(Menu menu) {
		logger.info("@@@Calling the menu service save proc----------------");

		try {
			// set the value to ID
			menu.setId(Long.parseLong(DateUtil.getUniqueID()));

			Long parentId = (menu.getParent() != null) ? menu.getParent().getId() : null;

			System.out.println("@@@Chaking the value of theis:" + parentId);

			// call the save proc
			Menu saveMenu = _menuRepository.saveAndFlush(menu);

			return new ServiceResponse<Menu>(true, "New Mneu Saved", saveMenu);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ServiceResponse<Menu>(false, "Failed to save information", null);
		}
	}

	// This following processes are to show the menu and list menu to the coller
	// Build parent-child menu tree (same as before)
	public List<MenuDTO> buildMenuTree(List<Menu> menus) {

		Map<Long, MenuDTO> dtoMap = new HashMap<>();
		List<MenuDTO> rootMenus = new ArrayList<>();

		// Step 1: Convert all Menu → MenuDTO
		for (Menu menu : menus) {
			MenuDTO dto = new MenuDTO(menu.getId(), menu.getMenuName(), menu.getMenuUrl(), menu.getIcon(),
					menu.getDisplayOrder());
			dtoMap.put(menu.getId(), dto);
		}

		// Step 2: Build parent-child relationship
		for (Menu menu : menus) {
			MenuDTO dto = dtoMap.get(menu.getId());

			if (menu.getParent() != null) {
				MenuDTO parentDto = dtoMap.get(menu.getParent().getId());

				if (parentDto != null) {
					parentDto.getChildren().add(dto);
				}
			} else {
				rootMenus.add(dto); // no parent = root menu
			}
		}

		return rootMenus;

	}

	// THIS IS COPIED FROM Chatgpt TO TEST WHETHER ITS WORKING OR NOT
	// DATE :30/03/2026

	// Get menus by user ID
	public List<MenuDTO> getMenusByUserId(Long userId) {

		User user = _userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

		// Collect all roles
		Set<Role> roles = user.getUserRoles().stream().map(UserRoles::getRole).collect(Collectors.toSet());

		// Collect all menus for these roles
		Set<Menu> menuSet = new HashSet<>();
		for (Role role : roles) {
			menuSet.addAll(_roleMenuMapRepository.findMenusByRoleId(role.getRoleId()));
		}

		// Convert to List<MenuDTO> with ordering and parent-child tree
		List<MenuDTO> menuList = menuSet
				.stream().sorted(Comparator.comparing(Menu::getDisplayOrder)).map(menu -> new MenuDTO(menu.getId(),
						menu.getMenuName(), menu.getMenuUrl(), menu.getIcon(), menu.getDisplayOrder()))
				.collect(Collectors.toList());

		// Build parent-child tree
		return buildMenuTree(menuList, menuList);
	}

	// Recursive tree builder
	private List<MenuDTO> buildMenuTree(List<MenuDTO> allMenus, List<MenuDTO> menuList) {
		Map<Long, MenuDTO> menuMap = allMenus.stream().collect(Collectors.toMap(MenuDTO::getId, m -> m));

		List<MenuDTO> rootMenus = new ArrayList<>();

		for (MenuDTO menu : menuList) {
			// You must fetch parent ID from the original Menu entity
			// Assume MenuDTO has a temporary parentId field or map from entity
			Long parentId = getParentIdFromOriginal(menu.getId());

			if (parentId == null) {
				rootMenus.add(menu);
			} else {
				MenuDTO parent = menuMap.get(parentId);
				if (parent != null) {
					parent.getChildren().add(menu);
				}
			}
		}

		// Optional: sort children by displayOrder
		rootMenus.forEach(this::sortChildrenRecursively);

		return rootMenus;
	}

	private void sortChildrenRecursively(MenuDTO menu) {
		menu.getChildren().sort(Comparator.comparing(MenuDTO::getDisplayOrder));
		menu.getChildren().forEach(this::sortChildrenRecursively);
	}

	// Temporary method to get parentId — you map it from Menu entity
	private Long getParentIdFromOriginal(Long menuId) {

		return null; // implement accordingly
	}

	@Override
	public List<Menu> getMenusLstByUser(Long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Cacheable(value = "userMenus", key = "#userId")
	public List<Menu> getMenusForUser(Long userId) {

		List<Menu> menus = _menuRepository.findMenusByUserId(userId);
		return menus;
	}

	
	
	// new procedure to get menu sorted
	//date : 07/04/2026
	
	@Transactional
	public List<MenuDTO> getSidebarMenus(Long userId) {

		// Step 1: Fetch menus assigned to the user
		List<Menu> userMenus = _menuRepository.findMenusByUserId(userId);

		// Step 2: Keep all menus needed for tree building
		Map<Long, Menu> menuMap = new HashMap<>();
		for (Menu menu : userMenus) {
			menuMap.put(menu.getId(), menu);
		}

		// Step 3: Include missing parent menus recursively
		for (Menu menu : userMenus) {
			Menu parent = menu.getParent();
			while (parent != null) {
				menuMap.putIfAbsent(parent.getId(), parent);
				parent = parent.getParent();
			}
		}

		// Step 4: Create DTO map
		Map<Long, MenuDTO> dtoMap = new HashMap<>();
		for (Menu menu : menuMap.values()) {
			MenuDTO dto = new MenuDTO();
			dto.setId(menu.getId());
			dto.setMenuName(menu.getMenuName());
			dto.setMenuUrl(menu.getMenuUrl());
			dto.setIcon(menu.getIcon());
			dto.setDisplayOrder(menu.getDisplayOrder());
			dto.setChildren(new ArrayList<>());
			dtoMap.put(menu.getId(), dto);
		}

		// Step 5: Build tree
		List<MenuDTO> roots = new ArrayList<>();

		for (Menu menu : menuMap.values()) {
			MenuDTO dto = dtoMap.get(menu.getId());

			if (menu.getParent() != null) {
				MenuDTO parentDto = dtoMap.get(menu.getParent().getId());
				if (parentDto != null) {
					parentDto.getChildren().add(dto);
				} else {
					roots.add(dto);
				}
			} else {
				roots.add(dto);
			}
		}

		// Step 6: Sort root and child menus
		sortMenus(roots);

		return roots;
	}

	private void sortMenus(List<MenuDTO> menus) {
		if (menus == null || menus.isEmpty()) {
			return;
		}

		menus.sort(Comparator.comparing(MenuDTO::getDisplayOrder, Comparator.nullsLast(Integer::compareTo)));

		for (MenuDTO menu : menus) {
			sortMenus(menu.getChildren());
		}
	}

	// get menu to sidebar
	@Transactional
	public List<MenuDTO> getSidebarMenus_Old(Long userId) {

		// Step 1: Fetch menus assigned to the user (flat)
		List<Menu> userMenus = _menuRepository.findMenusByUserId(userId);

		// Step 2: Create DTOs map
		Map<Long, MenuDTO> dtoMap = new HashMap<>();
		for (Menu m : userMenus) {
			dtoMap.put(m.getId(), new MenuDTO(m.getId(), m.getMenuName(), m.getMenuUrl()));
		}

		// Step 3: Include parent menus if missing
		for (Menu m : userMenus) {
			if (m.getParent() != null && !dtoMap.containsKey(m.getParent().getId())) {
				Menu p = m.getParent();
				dtoMap.put(p.getId(), new MenuDTO(p.getId(), p.getMenuName(), p.getMenuUrl()));
			}
		}

		// Step 4: Build tree
		List<MenuDTO> roots = new ArrayList<>();
		for (MenuDTO dto : dtoMap.values()) {
			Menu m = findMenuById(userMenus, dto.getId());
			if (m != null && m.getParent() != null) {
				MenuDTO parentDto = dtoMap.get(m.getParent().getId());
				if (parentDto != null) {
					parentDto.getChildren().add(dto);
				} else {
					roots.add(dto); // fallback
				}
			} else {
				roots.add(dto); // root menu
			}
		}
		return roots;
	}

	// Helper method
	private Menu findMenuById(List<Menu> menus, Long id) {
		for (Menu m : menus) {
			if (m.getId().equals(id))
				return m;
		}
		return null;

	}

	// get menutree

	public List<MenuDTO> getAllMenusTree() {

		List<Menu> allMenus = _menuRepository.findAll();

		Map<Long, MenuDTO> dtoMap = new HashMap<>();

		// Step 1: Convert to DTO
		for (Menu m : allMenus) {
			System.out.println("@@@ getAllMenusTree value :" + m.getMenuName());
			dtoMap.put(m.getId(), new MenuDTO(m.getId(), m.getMenuName(), m.getMenuUrl()));
		}

		// Step 2: Build tree
		List<MenuDTO> roots = new ArrayList<>();

		for (Menu m : allMenus) {
			System.out.println("@@@ getAllMenusTree to menu value :" + m.getMenuName());
			MenuDTO dto = dtoMap.get(m.getId());

			if (m.getParent() != null) {
				System.out.println("   Parent ID: " + m.getParent().getId());
				MenuDTO parentDto = dtoMap.get(m.getParent().getId());
				if (parentDto != null) {
					parentDto.getChildren().add(dto);
				}
			} else {

				roots.add(dto);
			}
		}

		return roots;
	}

	// Today changes
	// date/01/04/2026/

}
